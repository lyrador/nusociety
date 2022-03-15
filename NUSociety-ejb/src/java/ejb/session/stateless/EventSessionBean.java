/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Event;
import entity.EventCategory;
import entity.Society;
import entity.Student;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EventAlreadyExistsException;
import util.exception.EventNotFoundException;

/**
 *
 * @author ajayan
 */
@Stateless
public class EventSessionBean implements EventSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public List<Event> retrieveAllEvents() {
        Query query = em.createQuery("SELECT e FROM Event e");     
        return query.getResultList();
    }
    
    @Override
    public Event retrieveEventById(Long eventId) throws EventNotFoundException {
        Event e = em.find(Event.class, eventId);
        
        if(eventId != null) {
            return e; 
        } else {
            throw new EventNotFoundException("Error, Event with ID: " + eventId + " not found!"); 
        }
    }
    
    @Override
    public Event retrieveEventByName(String eventName) throws EventNotFoundException {
        Query query = em.createQuery("SELECT e from Event e WHERE e.eventName = :inEventName"); 
        query.setParameter("inEventName", eventName); 
        
        try {
            return (Event) query.getSingleResult(); 
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EventNotFoundException("Error, Event with Name: " + eventName + "cannot be found."); 
        }
    }
    
    @Override
    public Long createNewEvent(Event event) /*throws EventAlreadyExistsException*/ {
        em.persist(event);
        em.flush();
        return event.getEventId();
            //} else { 
             //   throw new EventAlreadyExistsException("Error, Event with ID: " + event.getEventId() + " already exists!");
  
    }
    
    public void updateEvent(Event event, Long updateEventId) throws EventNotFoundException {
        System.out.println("******EVENT ID IS + " + event.getEventId() + " *******"); 
        Event e = retrieveEventById(updateEventId); 
        
        if (e.getEventName() != null) {
            e.setEventName(event.getEventName());
        } 
        if (e.getEventDetails() != null) {
            e.setEventDetails(event.getEventDetails());
        } 
        if (e.getEventLocation() != null) {
            e.setEventLocation(event.getEventLocation());
        }
        //if (e.getEventCapacity() != -1) {
            e.setEventCapacity(event.getEventCapacity());
       // }
        if (e.getEventDateStart() != null) {
            e.setEventDateStart(event.getEventDateStart());
        }
        if (e.getEventDateEnd() != null) {
            e.setEventDateEnd(event.getEventDateEnd());
        }
        
        System.out.println("******EVENT Details IS + " + event.getEventDetails()+ " *******"); 
    }
    
    public void deleteEvent(Long eventId) throws EventNotFoundException {
        Event eventToBeDeleted = retrieveEventById(eventId); 
        
        for (Student student : eventToBeDeleted.getStudents()) {
            em.remove(student);
        }
        eventToBeDeleted.getStudents().clear();
        em.remove(eventToBeDeleted);
        
        eventToBeDeleted.getStudent().getEventsOrganised().remove(eventToBeDeleted); 
        eventToBeDeleted.getSociety().getEvents().remove(eventToBeDeleted);
        
        for(EventCategory category: eventToBeDeleted.getCategories()) {
            
            category.getEvents().remove(eventToBeDeleted);            
            if (category.getEvents().size() == 0) {                  
                em.remove(eventToBeDeleted);
            }
        }       
        eventToBeDeleted.getCategories().clear();
        
        
        em.remove(eventToBeDeleted);                
    }
    
  /*  @Override
    public Society retrieveSocietyById(Long societyId) {
        return em.find(Society.class, societyId);
    }*/
    
/*    @Override
     public Long createNewSociety(Society society) /*throws EventAlreadyExistsException*/ //{
/*        em.persist(society);
        em.flush();
        return society.getSocietyId();
     }
*/

    
}
