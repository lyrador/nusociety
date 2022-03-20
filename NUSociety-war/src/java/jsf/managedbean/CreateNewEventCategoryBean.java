/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventCategorySessionBeanLocal;
import entity.EventCategory;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.EventCategoryAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "createNewEventCategoryBean")
@RequestScoped
public class CreateNewEventCategoryBean {

    @EJB(name = "EventCategorySessionBeanLocal")
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;
    
    

    private EventCategory newEventCategory; 
    
    public CreateNewEventCategoryBean() {
        newEventCategory = new EventCategory(); 
    }
    
     public void doCreateNewEventCategory(ActionEvent event) throws EventCategoryAlreadyExistsException {
         try {
             eventCategorySessionBeanLocal.createNewEventCategory(getNewEventCategory()); 
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New event category created ID: " + getNewEventCategory().getEventCategoryId(),"New event created ID: " + getNewEventCategory().getEventCategoryId()));
         } catch (EventCategoryAlreadyExistsException ex) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Event Category: " + ex.getMessage(), null));
         }
     }

    public EventCategory getNewEventCategory() {
        return newEventCategory;
    }

    public void setNewEventCategory(EventCategory newEventCategory) {
        this.newEventCategory = newEventCategory;
    }
     
     
    
}
