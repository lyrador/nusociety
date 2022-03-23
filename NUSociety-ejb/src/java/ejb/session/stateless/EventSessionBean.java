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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EventAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;
import util.exception.EventNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author ajayan
 */
@Stateless
public class EventSessionBean implements EventSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
     
    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal; 


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
        
            try {
                Society society = societySessionBeanLocal.retrieveSocietyById(event.getSociety().getSocietyId()); 
                event.setSociety(society);
            } 
            catch (SocietyNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            try {
                Student student = studentSessionBeanLocal.retrieveStudentByStudentId(event.getStudent().getStudentId()); 
                event.setStudent(student);
            }
            catch (StudentNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            List<EventCategory> categories = event.getCategories(); 
            for (EventCategory category : categories) {
                EventCategory categoryToSet; 
                try {
                    categoryToSet = eventCategorySessionBeanLocal.retrieveEventCategoryById(category.getEventCategoryId());
                    if (!event.getCategories().contains(categoryToSet)){
                    event.getCategories().add(categoryToSet);
                    }
                } catch (EventCategoryNotFoundException ex) {
                    Logger.getLogger(EventSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            em.flush();
        
        return event.getEventId();
  
    }
    
    public void updateEvent(Event event) throws EventNotFoundException {
        System.out.println("******EVENT ID IS + " + event.getEventId() + " *******"); 
        Event e = retrieveEventById(event.getEventId()); 

        e.setEventName(event.getEventName());

        e.setEventDetails(event.getEventDetails());

        e.setEventLocation(event.getEventLocation());

        e.setEventCapacity(event.getEventCapacity());

        e.setEventDateStart(event.getEventDateStart());

        e.setEventDateEnd(event.getEventDateEnd());
        
        e.setSociety(event.getSociety());
        e.setStudent(event.getStudent());
        
        //List<EventCategory> categoriesToRemove = e.getCategories(); 
   
        e.getCategories().clear();
        
        for (EventCategory categoryUpdated : event.getCategories()) {
            System.out.println("***** " + categoryUpdated.getCategoryName() + " *****"); 
            e.getCategories().add(categoryUpdated); 
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

