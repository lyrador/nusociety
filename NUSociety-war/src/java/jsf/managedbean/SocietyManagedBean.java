/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import entity.Event;
import entity.Society;
import entity.Student;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.exception.EventNotFoundException;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "societyManagedBean")
@ViewScoped
public class SocietyManagedBean implements Serializable {

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    private RosterManagementManagedBean rosterManagementManagedBean;

    private Student student;
    private Society society; 
    private Event event; 
    private List<Event> privateSocietyEvents; 
    private List<Event> publicSocietyEvents;
    
    private ScheduleModel scheduleModel;
    private ScheduleEvent scheduleEvent; 
    
    public SocietyManagedBean() {
        scheduleModel = new DefaultScheduleModel();
        scheduleEvent = new DefaultScheduleEvent(); 
        event = new Event(); 
    }
    
    @PostConstruct
    public void init() {
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            society = societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId));
            List<Event> events = society.getEvents(); 
            setPrivateSocietyEvents(eventSessionBeanLocal.retrievePrivateEventsForSociety(society.getSocietyId())); 
            publicSocietyEvents = eventSessionBeanLocal.retrievePublicEventsForSociety(society.getSocietyId()); 
            if (student.getMemberSocieties().contains(society)) {
                for (Event event : events) {
                    getScheduleModel().addEvent(new DefaultScheduleEvent(event.getEventName(), Instant.ofEpochMilli(event.getEventDateStart().getTime())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime(), Instant.ofEpochMilli(event.getEventDateEnd().getTime())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()));
                }
            } else {
                for (Event event : publicSocietyEvents) {
                getScheduleModel().addEvent(new DefaultScheduleEvent(event.getEventName(), Instant.ofEpochMilli(event.getEventDateStart().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime(), Instant.ofEpochMilli(event.getEventDateEnd().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime()));
                }           
            }
    /*      for (Event event : getPrivateSocietyEvents()) {
                getScheduleModel().addEvent(new DefaultScheduleEvent(event.getEventName(), Instant.ofEpochMilli(event.getEventDateStart().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime(), Instant.ofEpochMilli(event.getEventDateEnd().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime()));
            }
            
            */
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
    }
    
    public void onEventSelect(SelectEvent selectEvent) throws EventNotFoundException 
    {
        scheduleEvent = (ScheduleEvent) selectEvent.getObject();
        this.event = eventSessionBeanLocal.retrieveEventByName(scheduleEvent.getTitle()); 
    }
    
    public void addEvent(ActionEvent actionEvent) 
    {
        if(scheduleEvent.getId() == null)
            scheduleModel.addEvent(scheduleEvent);
        else
            scheduleModel.updateEvent(scheduleEvent);
         
        scheduleEvent = new DefaultScheduleEvent();
    }
    
    public void onDateSelect(SelectEvent selectEvent) 
    {
        scheduleEvent = new DefaultScheduleEvent("", (LocalDateTime) selectEvent.getObject(), (LocalDateTime) selectEvent.getObject());
    }
    
    
    
    public void onEventMove(ScheduleEntryMoveEvent scheduleEvent) 
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + scheduleEvent.getDayDelta() + ", Minute delta:" + scheduleEvent.getMinuteDelta());
         
        addMessage(message);
    }
    
    
    
    public void onEventResize(ScheduleEntryResizeEvent scheduleEvent) 
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + scheduleEvent.getDayDeltaStart() + " to " + scheduleEvent.getDayDeltaEnd() + ", Minute delta:" + scheduleEvent.getMinuteDeltaStart() + " to " + scheduleEvent.getMinuteDeltaEnd());
         
        addMessage(message);
    }
     
    
    
    private void addMessage(FacesMessage message) 
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    
    
    private Calendar today() 
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ScheduleModel getScheduleModel() {
        return scheduleModel;
    }

    public void setScheduleModel(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }

    public ScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public void setScheduleEvent(ScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }
    
    public boolean isLeaderOfSociety() {
        for (Student leaderStudent : society.getLeaderStudents()) {
            if (leaderStudent.getStudentId() == student.getStudentId()) { 
                return true;
            }
        }
        return false;
    }
    
    public boolean isMemberOfSociety() {
        for (Student memberStudent : society.getMemberStudents()) {
            if (memberStudent.getStudentId() == student.getStudentId()) { 
                return true;
            }
        }
        return false;
    }

    public List<Event> getPrivateSocietyEvents() {
        return privateSocietyEvents;
    }

    public void setPrivateSocietyEvents(List<Event> privateSocietyEvents) {
        this.privateSocietyEvents = privateSocietyEvents;
    }

    public List<Event> getPublicSocietyEvents() {
        return publicSocietyEvents;
    }

    public void setPublicSocietyEvents(List<Event> publicSocietyEvents) {
        this.publicSocietyEvents = publicSocietyEvents;
    }
    
    
}
