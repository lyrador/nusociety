/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import entity.Announcement;
import entity.Society;
import entity.SocietyCategory;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateSocietyException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author raihan
 */
@Named(value = "societyManagementManagedBean")
@ViewScoped
public class SocietyManagementManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    
    private List<Society> societies;
    private List<SocietyCategory> societyCategories;
    private List<Announcement> announcements;
    
    private Society newSociety;
    private List<Long> newCategoryIds;
    private List<Long> newStaffIds;
    
    public SocietyManagementManagedBean() {
        newSociety = new Society();
    }
    
    @PostConstruct
    public void postConstruct() {
        societies = societySessionBeanLocal.retrieveAllSocieties();
        societyCategories = societyCategorySessionBeanLocal.retrieveAllSocietyCategories();
        announcements = announcementSessionBeanLocal.retrieveAllAnnouncements();
    }
    
    public void createNewSociety(ActionEvent event) {
        
        try {
            Society society = societySessionBeanLocal.createNewSociety(newSociety, newCategoryIds, newStaffIds);
            societies.add(society);
            
            newSociety = new Society();
            newCategoryIds = null;
            newStaffIds = null;
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New society created successfully (Product ID: " + society.getSocietyId() + ")", null));

            
        } catch (CreateSocietyException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new society: " + ex.getMessage(), null));
        }
        
    }
    
    
}
