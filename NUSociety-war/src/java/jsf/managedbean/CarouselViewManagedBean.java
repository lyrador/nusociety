/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import entity.Event;
import entity.Society;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author ajayan
 */
@Named
@ViewScoped
public class CarouselViewManagedBean implements Serializable {

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    private List<Event> events; 
    private String societyId; 
    
    @PostConstruct
    public void init() {
        
       // Society currentSociety = (Society) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentSociety");
        this.societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId"); 
        
        setEvents(eventSessionBeanLocal.retrieveEventsForSociety(Long.parseLong(societyId))); 
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }
    
    
    
    
}
