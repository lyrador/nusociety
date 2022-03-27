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
                society.getEvents().add(event); 
            } 
            catch (SocietyNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            try {
                Student student = studentSessionBeanLocal.retrieveStudentByStudentId(event.getStudent().getStudentId()); 
                event.setStudent(student);
                student.getEventsOrganised().add(event); 
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
                    categoryToSet.getEvents().add(event); 
                    }
                } catch (EventCategoryNotFoundException ex) {
                    Logger.getLogger(EventSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            em.flush();
        
        return event.getEventId();
  
    }
    
    @Override
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
            categoryUpdated.getEvents().add(e); 
        }
        
        
        System.out.println("******EVENT Details IS + " + event.getEventDetails()+ " *******"); 
    }
    
    @Override
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
    
    @Override
    public Long joinEvent(Event joinEvent) throws EventNotFoundException {
        Long joinEventId = joinEvent.getEventId(); 
        try {
             Event storedJoinEvent = retrieveEventById(joinEventId);
              System.out.println("******** Stored Join Event name is " + storedJoinEvent.getEventName()); 
              if (storedJoinEvent.getStudents().isEmpty()) {
                  System.out.println("******** there are no students signed up for this event"); 
              } else {
                  for (Student student : storedJoinEvent.getStudents()) {
                      System.out.println("****** " + student.getName()); 
                  }
              }
             
             if (storedJoinEvent.getStudents() != null || !storedJoinEvent.getStudents().isEmpty()) {
                List<Student> studentsAlreadySignedUp = storedJoinEvent.getStudents();
                Student studentWantsToJoin = joinEvent.getStudents().get(joinEvent.getStudents().size() - 1); 
                System.out.println("******** Student wants to join name is " + studentWantsToJoin.getName()); 
                studentsAlreadySignedUp.add(studentWantsToJoin); 
                studentWantsToJoin.getEvents().add(storedJoinEvent); 
                storedJoinEvent.setStudents(studentsAlreadySignedUp);
                //em.persist(storedJoinEvent);
             
             }else {
                Student studentWantsToJoin = joinEvent.getStudents().get(joinEvent.getStudents().size() - 1); 
                storedJoinEvent.getStudents().add(studentWantsToJoin); 
                studentWantsToJoin.getEvents().add(storedJoinEvent);
                storedJoinEvent.setStudents(storedJoinEvent.getStudents()); 
                //em.persist(storedJoinEvent);
             }
             
             return storedJoinEvent.getEventId(); 
        }
        catch (EventNotFoundException ex) 
        {
          System.out.println(ex.getMessage());    
        }
        
        return joinEvent.getEventId(); 
    }
    
    @Override
    public Long leaveEvent (Event leaveEvent, Student currentStudent) throws EventNotFoundException {
        Long leaveEventId = leaveEvent.getEventId(); 
        try {
            Event storedLeaveEvent = retrieveEventById(leaveEventId); 
            List<Student> currentStudentsList = storedLeaveEvent.getStudents(); 
            currentStudentsList.remove(currentStudent); 
            currentStudent.getEvents().remove(storedLeaveEvent); 
            storedLeaveEvent.setStudents(currentStudentsList);
        }
        catch (EventNotFoundException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return leaveEvent.getEventId(); 
                
    }
    
    @Override
    public List<Event> retrieveEventsForStudent(Long studentId) {
        Query query = em.createQuery("SELECT e FROM Event e, IN (e.students) s WHERE s.studentId = :inStudentId"); 
        query.setParameter("inStudentId", studentId); 
        List<Event> eventsRegistered = query.getResultList(); 
        
        for (Event event : eventsRegistered) {
            event.getCategories().size(); 
        }
        
        return query.getResultList(); 
    }
    
    @Override
    public List<Event> retrieveEventsForSociety(Long societyId) {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.society.societyId = :inSocietyId"); 
        query.setParameter("inSocietyId", societyId); 
        List<Event> societyEvents = query.getResultList(); 
        
        for (Event event: societyEvents) {
            event.getCategories().size(); 
        }
        
        return query.getResultList(); 
    }
}

