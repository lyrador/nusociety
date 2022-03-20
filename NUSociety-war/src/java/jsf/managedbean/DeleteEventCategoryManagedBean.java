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
import util.exception.EventCategoryNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "deleteEventCategoryManagedBean")
@RequestScoped
public class DeleteEventCategoryManagedBean {

    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;
    
    private EventCategory eventCategoryDelete; 
    private Long eventCategoryDeleteId; 

    
    public DeleteEventCategoryManagedBean() {
        eventCategoryDelete = new EventCategory(); 
        eventCategoryDeleteId = this.eventCategoryDeleteId; 
    }
    
    public void doDeleteEventCategory(ActionEvent event) throws EventCategoryNotFoundException {
        try {
        eventCategorySessionBeanLocal.deleteEventCategory(getEventCategoryDeleteId()); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event Category deleted Id: " + getEventCategoryDeleteId() ,"Event Category deleted: " + getEventCategoryDeleteId()));
        } catch(EventCategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public EventCategory getEventCategoryDelete() {
        return eventCategoryDelete;
    }

    public void setEventCategoryDelete(EventCategory eventCategoryDelete) {
        this.eventCategoryDelete = eventCategoryDelete;
    }

    public Long getEventCategoryDeleteId() {
        return eventCategoryDeleteId;
    }

    public void setEventCategoryDeleteId(Long eventCategoryDeleteId) {
        this.eventCategoryDeleteId = eventCategoryDeleteId;
    }
    
    
    
}
