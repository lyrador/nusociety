/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import entity.Event;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.EventNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "updateEventManagedBean")
@RequestScoped
public class UpdateEventManagedBean {

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;
    
    private Event updateEvent;
    private Long updateEventId; 

    /**
     * Creates a new instance of UpdateEventManagedBean
     */
    public UpdateEventManagedBean() {
        updateEvent = new Event(); 
        updateEventId = this.updateEventId; 
    }
    
    public void doUpdateEvent(ActionEvent event) throws EventNotFoundException {
        try {
            //updateEvent = eventSessionBeanLocal.retrieveEventById(updateEventId); 
            eventSessionBeanLocal.updateEvent(updateEvent, updateEventId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated: " + updateEvent,"Event updated: " + updateEvent));
        } catch (EventNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Event: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }

    public Event getUpdateEvent() {
        return updateEvent;
    }

    public Long getUpdateEventId() {
        return updateEventId;
    }

    public void setUpdateEventId(Long updateEventId) {
        this.updateEventId = updateEventId;
    }
    
    
    
}
