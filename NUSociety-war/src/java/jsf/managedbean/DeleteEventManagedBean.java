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
@Named(value = "deleteEventManagedBean")
@RequestScoped
public class DeleteEventManagedBean {

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;
    
    private Event eventDelete; 
    private Long eventDeleteId; 
    
    
    public DeleteEventManagedBean() {
        eventDelete = new Event(); 
        eventDeleteId = this.eventDeleteId; 
    }
    
    public void doDeleteEvent(ActionEvent event) throws EventNotFoundException {
        try {
        eventSessionBeanLocal.deleteEvent(eventDeleteId); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event deleted Id: " + getEventDeleteId(),"Event deleted: " + getEventDeleteId()));
        } catch(EventNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public Event getEventDelete() {
        return eventDelete;
    }

    public void setEventDelete(Event eventDelete) {
        this.eventDelete = eventDelete;
    }

    public Long getEventDeleteId() {
        return eventDeleteId;
    }

    public void setEventDeleteId(Long eventDeleteId) {
        this.eventDeleteId = eventDeleteId;
    }
    
    
    
}
