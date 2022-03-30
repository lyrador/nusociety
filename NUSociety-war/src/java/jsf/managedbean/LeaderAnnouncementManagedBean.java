/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Announcement;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "leaderAnnouncemmentManagedBean")
@ViewScoped
public class LeaderAnnouncementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    
    private List<Student> students;
    private Society currentSociety;
    private Announcement newAnnouncement;
    private String viewOption;
    
    public LeaderAnnouncementManagedBean() {
        newAnnouncement = new Announcement();
        newAnnouncement.setCreationDate(new Date());
    }
    
    @PostConstruct
    public void postConstruct() {  
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        try {
            setCurrentSociety(societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId)));
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
        this.setStudents(studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(getCurrentSociety().getSocietyId()));
    }
    
    public void createNewAnnouncement(ActionEvent event) throws SocietyNotFoundException {
        if (viewOption.equals("Followers")) {
            newAnnouncement.setIsOnlyForMembers(false);
        } else {
            newAnnouncement.setIsOnlyForMembers(true);
        }
        Announcement createdAnnouncement = announcementSessionBeanLocal.createNewAnnouncement(getNewAnnouncement(), getCurrentSociety().getSocietyId());      
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New announcement created: " + getNewAnnouncement().getAnnouncementId(),"New announcement created: " + getNewAnnouncement().getAnnouncementId())); 
        newAnnouncement = new Announcement();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Society getCurrentSociety() {
        return currentSociety;
    }
    
    public void setCurrentSociety(Society currentSociety) {
        this.currentSociety = currentSociety;
    }

    public Announcement getNewAnnouncement() {
        return newAnnouncement;
    }

    public void setNewAnnouncement(Announcement newAnnouncement) {
        this.newAnnouncement = newAnnouncement;
    }

    public String getViewOption() {
        return viewOption;
    }

    public void setViewOption(String viewOption) {
        this.viewOption = viewOption;
    }   
}