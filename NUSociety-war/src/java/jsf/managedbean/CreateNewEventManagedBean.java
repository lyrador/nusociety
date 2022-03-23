/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBean;
import ejb.session.stateless.StudentSessionBeanLocal;
import ejb.session.stateless.EventSessionBeanLocal; 
import entity.Event;
import entity.EventCategory;
import entity.Society;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.EventAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "createNewEventManagedBean")
@RequestScoped
public class CreateNewEventManagedBean {

    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    

    private Event newEvent; 
    private Long newStudentId; 
    private Long newSocietyId; 
    private Long newEventCategoryId; 
    
    
    public CreateNewEventManagedBean() {
        newEvent = new Event();
        newStudentId = this.newStudentId; 
        newSocietyId = this.newSocietyId; 
        newEventCategoryId = this.newEventCategoryId; 
    }
    
    public void doCreateNewEvent(ActionEvent event) throws EventAlreadyExistsException, StudentNotFoundException, SocietyNotFoundException, EventCategoryNotFoundException {
        try {
        
        newEvent.setStudent(studentSessionBeanLocal.retrieveStudentByStudentId(getNewStudentId()));
        newEvent.setSociety(societySessionBeanLocal.retrieveSocietyById(newSocietyId));
        if (newEvent.getCategories() != null) {
        newEvent.getCategories().add(eventCategorySessionBeanLocal.retrieveEventCategoryById(getNewEventCategoryId()));
        } else {
            List<EventCategory> categories = new ArrayList<EventCategory>(); 
            categories.add(eventCategorySessionBeanLocal.retrieveEventCategoryById(getNewEventCategoryId())); 
            newEvent.setCategories(categories);
        }
        //Society dummySociety = new Society("Flaggers", "We Love Flags!", new Date());
        //eventSessionBeanLocal.createNewSociety(dummySociety); 
        //newEvent.setSociety(dummySociety);
        
        Long newEventId = eventSessionBeanLocal.createNewEvent(newEvent); 
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New event created: " + newEventId,"New event created: " + newEventId));
        
        } catch(StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Event: " + ex.getMessage(), null));
        }
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }
    
    public Long getNewStudentId() {
        return newStudentId;
    }

    public void setNewStudentId(Long newStudentId) {
        this.newStudentId = newStudentId;
    }

    public Long getNewSocietyId() {
        return newSocietyId;
    }

    public void setNewSocietyId(Long newSocietyId) {
        this.newSocietyId = newSocietyId;
    }

    public Long getNewEventCategoryId() {
        return newEventCategoryId;
    }

    public void setNewEventCategoryId(Long newEventCategoryId) {
        this.newEventCategoryId = newEventCategoryId;
    }
    
    
    
}
