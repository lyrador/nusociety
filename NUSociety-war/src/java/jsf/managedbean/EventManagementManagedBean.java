/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventCategorySessionBeanLocal;
import ejb.session.stateless.EventSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Event;
import entity.EventCategory;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import util.exception.EventAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;
import util.exception.EventNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author ajayan
 */
@Named(value = "eventManagementManagedBean")
@ViewScoped
public class EventManagementManagedBean implements Serializable {

    
    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal1;

    @EJB
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    

    private List<Event> events;
    private List<Event> filteredEvents; 
    //private List<EventCategory> categories; 
    //private List<Society> societies; 
    //private List<Student> students; 

    private Event newEvent;
    private Long newStudentId;
    private Long newSocietyId;
    private Long newEventCategoryId;

    private Event updateEvent;
    //private Long updateEventId;

    //private Event eventDelete;
    //private Long eventDeleteId;
    
    private List<SelectItem> selectItemsSocietyObject; 
    private List<SelectItem> selectItemsSocietyName; 
    
    private List<SelectItem> selectItemsCategoryObject; 
    private List<SelectItem> selectItemsCategoryName; 
    
    private List<SelectItem> selectItemsStudentObject; 
    private List<SelectItem> selectItemsStudentName; 
    
    
    private Event viewEvent; 

    public EventManagementManagedBean() {
        newEvent = new Event();
        updateEvent = new Event();
        //eventDelete = new Event();
        viewEvent = new Event(); 
        selectItemsSocietyObject = new ArrayList<>(); 
        selectItemsSocietyName = new ArrayList<>();
        selectItemsCategoryObject = new ArrayList<>();
        selectItemsCategoryName = new ArrayList<>();
        selectItemsStudentObject = new ArrayList<>();
        selectItemsStudentName = new ArrayList<>();
        
    }

    @PostConstruct
    public void postConstruct() {
        events = eventSessionBeanLocal.retrieveAllEvents();
        
        List<EventCategory> categories = eventCategorySessionBeanLocal.retrieveAllEvents();
       // this.setCategories(categories);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.categories", categories);
        
        List<Student> students = studentSessionBeanLocal.retrieveAllStudents(); 
        //this.setStudents(students);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.students", students);
        
        List<Society> societies = societySessionBeanLocal.retrieveAllSocieties();
        //this.setSocieties(societies);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.societies", societies);
        
        for (Society society: societies) 
        {
            selectItemsSocietyObject.add(new SelectItem(society, society.getName()));
            selectItemsSocietyName.add(new SelectItem(society.getName(), society.getName()));
        }
        
        for (Student student: students) 
        {
            selectItemsStudentObject.add(new SelectItem(student, student.getName()));
            selectItemsStudentName.add(new SelectItem(student.getName(), student.getName()));
        }
        
        for (EventCategory category: categories) 
        {
            selectItemsCategoryObject.add(new SelectItem(category, category.getCategoryName()));
            selectItemsCategoryName.add(new SelectItem(category.getCategoryName(), category.getCategoryName()));
        }
    }
    
    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.societies", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("EventManagementManagedBean.societies");
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.categories", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("EventManagementManagedBean.categories");
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.students", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("EventManagementManagedBean.students");
        
    }

    public void doCreateNewEvent(ActionEvent event) throws EventAlreadyExistsException, StudentNotFoundException, SocietyNotFoundException, EventCategoryNotFoundException {
        //try {

            //getNewEvent().setStudent(studentSessionBeanLocal.retrieveStudentByStudentId(getNewStudentId()));
            //getNewEvent().setSociety(societySessionBeanLocal.retrieveSocietyById(getNewSocietyId()));
        /*    if (getNewEvent().getCategories() != null) {
                getNewEvent().getCategories().add(eventCategorySessionBeanLocal.retrieveEventCategoryById(getNewEventCategoryId()));
            } else {
                List<EventCategory> categories = new ArrayList<EventCategory>();
                categories.add(eventCategorySessionBeanLocal.retrieveEventCategoryById(getNewEventCategoryId()));
                getNewEvent().setCategories(categories);
            }*/
            
            Long newEventId = eventSessionBeanLocal.createNewEvent(newEvent);
            newEvent.setEventId(newEventId);
            events.add(newEvent); 
            
            newEvent = new Event(); 

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New event created: " + newEventId, "New event created: " + newEventId));

       /* } catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Event: " + ex.getMessage(), null));
        }*/
    }
    
    public void doDeleteEvent(ActionEvent event) throws EventNotFoundException {
        try {
        Event eventToDelete = (Event)event.getComponent().getAttributes().get("eventToDelete"); 
        
        eventSessionBeanLocal.deleteEvent(eventToDelete.getEventId());
        events.remove(eventToDelete); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event deleted Id: " + eventToDelete.getEventId(),"Event deleted: " + eventToDelete.getEventId()));
        } catch(EventNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateEvent(ActionEvent event) throws EventNotFoundException {
        try {
            //updateEvent = eventSessionBeanLocal.retrieveEventById(updateEventId); 
            eventSessionBeanLocal.updateEvent(updateEvent);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated: " + updateEvent,"Event updated: " + updateEvent));
        } catch (EventNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Event: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }
    
    public void onDateSelect(SelectEvent<Date> event) {
        FacesContext facesContext = FacesContext.getCurrentInstance(); 
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format((event.getObject()))));
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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

    public Event getUpdateEvent() {
        return updateEvent;
    }

    public void setUpdateEvent(Event updateEvent) {
        this.updateEvent = updateEvent;
    }

   /* public Long getUpdateEventId() {
        return updateEventId;
    }

    public void setUpdateEventId(Long updateEventId) {
        this.updateEventId = updateEventId;
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

    public List<EventCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EventCategory> categories) {
        this.categories = categories;
    }*/

    public Event getViewEvent() {
        return viewEvent;
    }

    public void setViewEvent(Event viewEvent) {
        this.viewEvent = viewEvent;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(List<Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }

    /*public List<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(List<Society> societies) {
        this.societies = societies;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }*/
    
    public List<SelectItem> getSelectItemsSocietyObject() {
        return selectItemsSocietyObject;
    }

    public void setSelectItemsSocietyObject(List<SelectItem> selectItemsSocietyObject) {
        this.selectItemsSocietyObject = selectItemsSocietyObject;
    }

    public List<SelectItem> getSelectItemsSocietyName() {
        return selectItemsSocietyName;
    }

    public void setSelectItemsSocietyName(List<SelectItem> selectItemsSocietyName) {
        this.selectItemsSocietyName = selectItemsSocietyName;
    }

    public List<SelectItem> getSelectItemsCategoryObject() {
        return selectItemsCategoryObject;
    }

    public void setSelectItemsCategoryObject(List<SelectItem> selectItemsCategoryObject) {
        this.selectItemsCategoryObject = selectItemsCategoryObject;
    }

    public List<SelectItem> getSelectItemsCategoryName() {
        return selectItemsCategoryName;
    }

    public void setSelectItemsCategoryName(List<SelectItem> selectItemsCategoryName) {
        this.selectItemsCategoryName = selectItemsCategoryName;
    }

    public List<SelectItem> getSelectItemsStudentObject() {
        return selectItemsStudentObject;
    }

    public void setSelectItemsStudentObject(List<SelectItem> selectItemsStudentObject) {
        this.selectItemsStudentObject = selectItemsStudentObject;
    }

    public List<SelectItem> getSelectItemsStudentName() {
        return selectItemsStudentName;
    }

    public void setSelectItemsStudentName(List<SelectItem> selectItemsStudentName) {
        this.selectItemsStudentName = selectItemsStudentName;
    }

    


}