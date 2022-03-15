/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventCategorySessionBeanLocal;
import entity.EventCategory;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ajayan
 */
@Named(value = "viewAllEventCategoriesManagedBean")
@RequestScoped
public class ViewAllEventCategoriesManagedBean {

    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;
    
    private List<EventCategory> eventCategories; 

    public ViewAllEventCategoriesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() 
    {
        setEventCategories(eventCategorySessionBeanLocal.retrieveAllEvents()); 
    }

    public List<EventCategory> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(List<EventCategory> eventCategories) {
        this.eventCategories = eventCategories;
    }
    
    
    
}
