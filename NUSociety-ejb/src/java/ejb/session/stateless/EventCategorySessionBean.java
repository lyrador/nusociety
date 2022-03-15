/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Event;
import entity.EventCategory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EventCategoryAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;

/**
 *
 * @author ajayan
 */
@Stateless
public class EventCategorySessionBean implements EventCategorySessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
   public List<EventCategory> retrieveAllEvents() {
        Query query = em.createQuery("SELECT e FROM EventCategory e");     
        return query.getResultList();
    }
    
    @Override
    public EventCategory retrieveEventCategoryById(Long eventCategoryId) throws EventCategoryNotFoundException {
        EventCategory e = em.find(EventCategory.class, eventCategoryId);
        
        if(eventCategoryId != null) {
            return e; 
        } else {
            throw new EventCategoryNotFoundException("Error, Event Category with ID: " + eventCategoryId + " not found!"); 
        }
    }
    
    @Override
    public EventCategory retrieveEventCategoryByName(String eventCategoryName) throws EventCategoryNotFoundException {
        Query query = em.createQuery("SELECT e from EventCategory e WHERE e.eventCategoryName = :inEventCateegoryName"); 
        query.setParameter("inEventCategoryName", eventCategoryName); 
        
        try {
            return (EventCategory) query.getSingleResult(); 
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EventCategoryNotFoundException("Error, Event with Name: " + eventCategoryName + "cannot be found."); 
        }
    }
    
    @Override
    public Long createNewEventCategory(EventCategory eventCategory) throws EventCategoryAlreadyExistsException {
        if (eventCategory.getEventCategoryId()== null || em.find(EventCategory.class, eventCategory.getEventCategoryId()) == null) {
            em.persist(eventCategory);
            em.flush();
            return eventCategory.getEventCategoryId();
        } else {
            throw new EventCategoryAlreadyExistsException("Error, Event with ID: " + eventCategory.getEventCategoryId() + " already exists!"); 
        }
        
    }
    
    @Override
    public void updateEventCategory(EventCategory eventCategory, Long eventCategoryId) throws EventCategoryNotFoundException {
        EventCategory e = retrieveEventCategoryById(eventCategoryId); 
        
        if (e.getCategoryName() != null) {
            e.setCategoryName(eventCategory.getCategoryName());
        }
    }
    
    @Override
    public void deleteEventCategory(Long eventCategoryId) throws EventCategoryNotFoundException {
        EventCategory e = retrieveEventCategoryById(eventCategoryId); 
        
        for(Event event : e.getEvents()) {
            
            event.getCategories().remove(e);            
            if (event.getCategories().size() == 0) {                  
                em.remove(event);
            }
        }
        e.getEvents().clear();
        
        em.remove(e);
    }
}
