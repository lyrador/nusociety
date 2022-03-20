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
@Named(value = "updateEventCategoryManagedBean")
@RequestScoped
public class UpdateEventCategoryManagedBean {

    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;

    private EventCategory eventCategoryUpdate; 
    private Long eventCategoryUpdateId; 

    public UpdateEventCategoryManagedBean() {
        eventCategoryUpdate = new EventCategory();
        eventCategoryUpdateId = this.eventCategoryUpdateId;
    }
    
    
    
    public void doUpdateEventCategory(ActionEvent event) throws EventCategoryNotFoundException{
        try {
            eventCategorySessionBeanLocal.updateEventCategory(eventCategoryUpdate, eventCategoryUpdateId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated ID: " + getEventCategoryUpdateId(),"Event updated: " + getEventCategoryUpdateId()));
        } catch (EventCategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Event: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public EventCategory getEventCategoryUpdate() {
        return eventCategoryUpdate;
    }

    public void setEventCategoryUpdate(EventCategory eventCategoryUpdate) {
        this.eventCategoryUpdate = eventCategoryUpdate;
    }

    public Long getEventCategoryUpdateId() {
        return eventCategoryUpdateId;
    }

    public void setEventCategoryUpdateId(Long eventCategoryUpdateId) {
        this.eventCategoryUpdateId = eventCategoryUpdateId;
    }
    
    
    
}
