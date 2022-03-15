/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import ejb.session.stateless.StudentSessionBean;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Event;
import entity.Society;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.EventAlreadyExistsException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "createNewEventManagedBean")
@RequestScoped
public class CreateNewEventManagedBean {

    

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;
    
    //need society bean, then can remove get society method in event bean

    private Event newEvent; 
    private Long newStudentId; 
    private Long newSocietyId; 
    
    
    public CreateNewEventManagedBean() {
        newEvent = new Event();
        newStudentId = this.newStudentId; 
        newSocietyId = newSocietyId; 
    }
    
    public void doCreateNewEvent(ActionEvent event) throws EventAlreadyExistsException, StudentNotFoundException {
        try {
        
        newEvent.setStudent(studentSessionBeanLocal.retrieveStudentByStudentId(getNewStudentId()));
        //newEvent.setSociety(eventSessionBeanLocal.retrieveSocietyById(getNewSocietyId()));
        Society dummySociety = new Society("Flaggers", "We Love Flags!", new Date());
        eventSessionBeanLocal.createNewSociety(dummySociety); 
        newEvent.setSociety(dummySociety);
        
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
    
    
}