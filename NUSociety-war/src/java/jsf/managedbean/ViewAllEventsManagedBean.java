/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import entity.Event;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ajayan
 */
@Named(value = "viewAllEventsManagedBean")
@RequestScoped
public class ViewAllEventsManagedBean {

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;
    
    private List<Event> events; 

    
    
    public ViewAllEventsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() 
    {
        events = eventSessionBeanLocal.retrieveAllEvents(); 
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
    
    
}
