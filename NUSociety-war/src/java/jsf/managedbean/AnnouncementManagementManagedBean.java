/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import entity.Announcement;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.AnnouncementNotFoundException;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "announcementManagementManagedBean")
@SessionScoped
public class AnnouncementManagementManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    
    
    private List<Announcement> allAnnouncements;
    private Long societyId;
    private Announcement newAnnouncement;
    
    private Announcement announcementToUpdate;
    private Announcement announcementToDelete;
    
    public AnnouncementManagementManagedBean() {
        newAnnouncement = new Announcement();
        announcementToUpdate = new Announcement();
        announcementToDelete = new Announcement();
    }
    
    @PostConstruct
    public void postConstruct() {
        allAnnouncements = announcementSessionBeanLocal.retrieveAllAnnouncements();
    }
    
    public void createNewAnnouncement(ActionEvent event) {
        
        try {
            newAnnouncement.setCreationDate(new Date());
            Announcement announcement = announcementSessionBeanLocal.createNewAnnouncement(newAnnouncement, getSocietyId());
            allAnnouncements.add(announcement);

            newAnnouncement = new Announcement();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New announcement created successfully (Announcement ID: " + announcement.getAnnouncementId() + ")", null));
        } catch (SocietyNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new announcement: " + ex.getMessage(), null));

        }

    }
    
    public void updateAnnouncement(ActionEvent event) {
        
        try {
            announcementSessionBeanLocal.updateAnnouncement(getAnnouncementToUpdate());
            allAnnouncements = announcementSessionBeanLocal.retrieveAllAnnouncements();
            
            setAnnouncementToUpdate(new Announcement());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement updated successfully!", null));
        } catch(AnnouncementNotFoundException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        
    }
    
    public void deleteAnnouncement(ActionEvent event) {
        
        try {
            announcementSessionBeanLocal.deleteAnnouncement(getAnnouncementToDelete().getAnnouncementId());
            
            allAnnouncements.remove(getAnnouncementToDelete());
            allAnnouncements = announcementSessionBeanLocal.retrieveAllAnnouncements();
            
            setAnnouncementToDelete(new Announcement());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement deleted successfully!", null));
        } catch(AnnouncementNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting announcement: " + ex.getMessage(), null));
        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Announcement> getAllAnnouncements() {
        return allAnnouncements;
    }

    public void setAllAnnouncements(List<Announcement> allAnnouncements) {
        this.allAnnouncements = allAnnouncements;
    }

    public Announcement getNewAnnouncement() {
        return newAnnouncement;
    }

    public void setNewAnnouncement(Announcement newAnnouncement) {
        this.newAnnouncement = newAnnouncement;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Announcement getAnnouncementToUpdate() {
        return announcementToUpdate;
    }

    public void setAnnouncementToUpdate(Announcement announcementToUpdate) {
        this.announcementToUpdate = announcementToUpdate;
    }

    public Announcement getAnnouncementToDelete() {
        return announcementToDelete;
    }

    public void setAnnouncementToDelete(Announcement announcementToDelete) {
        this.announcementToDelete = announcementToDelete;
    }
    
    public List<Announcement> getAllAnnouncementsSortedByMostRecent() {
        List<Announcement> temp = new ArrayList<Announcement>();
        temp.addAll(allAnnouncements);
        Collections.reverse(temp);
        return temp;
    }
}
