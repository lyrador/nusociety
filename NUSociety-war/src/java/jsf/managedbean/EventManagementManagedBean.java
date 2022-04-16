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
//import org.primefaces.model.ScheduleModel;
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
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    private List<Event> events;
    private List<Event> publicEvents; 
    private List<Event> privateEvents;
    private List<Event> filteredEvents; 

    private Event newEvent;
    private Event updateEvent;
    private Event viewEvent; 
    private Event joinEvent; 
    private Event leaveEvent; 
    
    
    private List<SelectItem> selectItemsSocietyObject; 
    private List<SelectItem> selectItemsSocietyName; 
    
    private List<SelectItem> selectItemsCategoryObject; 
    private List<SelectItem> selectItemsCategoryName; 
    
    //private List<SelectItem> selectItemsStudentObject; 
    //private List<SelectItem> selectItemsStudentName; 
    
    private Long studentId; 
    private List<Event> registeredEvents; 
    
   /* private Date minDateTime; 
    private Date maxDateTime; 
    private Date today; 
    
 /* private Long societyId; 
    private List<Event> societyEvents; */
   

    public EventManagementManagedBean() {
        newEvent = new Event();
        updateEvent = new Event();
        viewEvent = new Event(); 
        joinEvent = new Event(); 
        leaveEvent = new Event(); 
        selectItemsSocietyObject = new ArrayList<>(); 
        selectItemsSocietyName = new ArrayList<>();
        selectItemsCategoryObject = new ArrayList<>();
        selectItemsCategoryName = new ArrayList<>();
        //selectItemsStudentObject = new ArrayList<>();
        //selectItemsStudentName = new ArrayList<>();

    }

    @PostConstruct
    public void postConstruct() {
        events = eventSessionBeanLocal.retrieveAllEvents();
        
        setPublicEvents(eventSessionBeanLocal.retrieveAllPublicEvents()); 
        privateEvents = eventSessionBeanLocal.retrieveAllPrivateEvents(); 
        
        List<EventCategory> categories = eventCategorySessionBeanLocal.retrieveAllEvents();
       // this.setCategories(categories);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.categories", categories);
        
        List<Student> students = studentSessionBeanLocal.retrieveAllStudents(); 
        //this.setStudents(students);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.students", students);
        
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        this.studentId = currentStudent.getStudentId(); 
        this.registeredEvents = eventSessionBeanLocal.retrieveEventsForStudent(studentId); 
        
        List<Society> societiesForStudent = societySessionBeanLocal.retrieveSocietiesForMember(this.studentId);
        //this.setSocieties(societies);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("EventManagementManagedBean.societies", societiesForStudent);
   /*   Society currentSociety = (Society) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentSociety");
        this.societyId = currentSociety.getSocietyId(); 
        this.societyEvents = eventSessionBeanLocal.retrieveEventsForSociety(societyId); */
        
        for (Society society: societiesForStudent) 
        {
            selectItemsSocietyObject.add(new SelectItem(society, society.getName()));
            selectItemsSocietyName.add(new SelectItem(society.getName(), society.getName()));
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
            newEvent.setStudent(studentSessionBeanLocal.retrieveStudentByStudentId(this.studentId));
            Long newEventId = eventSessionBeanLocal.createNewEvent(newEvent);
            newEvent.setEventId(newEventId);
            System.out.println("The status is " + newEvent.getPublicOrPrivate());
            events.add(newEvent); 
            if (newEvent.isEventIsPublic() == true) {
                publicEvents.add(newEvent); 
            } else {
                privateEvents.add(newEvent); 
            }
            
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
        if (eventToDelete.isEventIsPublic() == true) {
                publicEvents.remove(eventToDelete); 
            } else {
                privateEvents.remove(eventToDelete); 
            }
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
    
    public void doJoinEvent(ActionEvent event) throws EventNotFoundException {
        try {
            if (joinEvent.getStudents().size() < joinEvent.getEventCapacity()) {
                /*for (Student student : joinEvent.getStudents()) {
                System.out.println("******** there is " + student.getName() + " in this event******");
                }*/
                System.out.println("******** there are " + joinEvent.getStudents().size() + " students in this event******");
                joinEvent.getStudents().add(studentSessionBeanLocal.retrieveStudentByStudentId(studentId)); 
                Long joinEventId = eventSessionBeanLocal.joinEvent(joinEvent);
                joinEvent.setEventId(joinEventId);
                events.add(joinEvent);
                registeredEvents.add(joinEvent);
                if (joinEvent.isEventIsPublic() == true) {
                    publicEvents.add(joinEvent); 
                } else {
                    privateEvents.add(joinEvent); 
                }
                //setRegisteredEvents(registeredEvents);
                for (Event eventRegistered : registeredEvents) {
                    System.out.println("********** " + eventRegistered.getEventName());
                    if (registeredEvents.contains(eventSessionBeanLocal.retrieveEventById(joinEventId))) {
                        System.out.println("**********  + Yes it already contain");
                    }
                }
            } else if  (joinEvent.getStudents().size() >= joinEvent.getEventCapacity()){
                System.out.println("**********  Yes there would be an error *****");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to join this event because it has reached its maximum capacity!", "Unable to join this event because it has reached its maximum capacity!"));
            }
        } catch (EventNotFoundException ex) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while joining Event: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        
    }
    
    public void doLeaveEvent(ActionEvent event) throws EventNotFoundException {
        try {
            Student currentStudent = studentSessionBeanLocal.retrieveStudentByStudentId(studentId); 
            Long leaveEventId = eventSessionBeanLocal.leaveEvent(getLeaveEvent(), currentStudent); 
            getLeaveEvent().setEventId(leaveEventId);
            events.add(getLeaveEvent()); 
            if (leaveEvent.isEventIsPublic() == true) {
                publicEvents.add(leaveEvent); 
            } else {
                privateEvents.add(leaveEvent); 
            }
            registeredEvents.remove(getLeaveEvent()); 
        }
        catch(EventNotFoundException ex ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Event: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void onDateSelect(SelectEvent<Date> event) {
        FacesContext facesContext = FacesContext.getCurrentInstance(); 
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
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

  /*  public Long getNewStudentId() {
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
    }*/

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

   /* public List<SelectItem> getSelectItemsStudentObject() {
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
    }*/

    public Event getJoinEvent() {
        return joinEvent;
    }

    public void setJoinEvent(Event joinEvent) {
        this.joinEvent = joinEvent;
    }
    
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public Event getLeaveEvent() {
        return leaveEvent;
    }

    public void setLeaveEvent(Event leaveEvent) {
        this.leaveEvent = leaveEvent;
    }

    public boolean isLeaderOfSociety() {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        if (currentStudent.getLeaderSocieties().size() != 0) {
            return true;
        }
        return false;
    }

    public List<Event> getPublicEvents() {
        return publicEvents;
    }

    public void setPublicEvents(List<Event> publicEvents) {
        this.publicEvents = publicEvents;
    }

    public List<Event> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(List<Event> privateEvents) {
        this.privateEvents = privateEvents;
    }
}
