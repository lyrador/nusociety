/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Event;
import entity.Society;
import java.util.List;
import javax.ejb.Local;
import util.exception.EventAlreadyExistsException;
import util.exception.EventNotFoundException;

/**
 *
 * @author ajayan
 */
@Local
public interface EventSessionBeanLocal {

    public List<Event> retrieveAllEvents();

    public Event retrieveEventById(Long eventId) throws EventNotFoundException;

    public Event retrieveEventByName(String eventName) throws EventNotFoundException;

    public Long createNewEvent(Event event) throws EventAlreadyExistsException;

    public void updateEvent(Event event, Long updateEventId) throws EventNotFoundException;

    public void deleteEvent(Long eventId) throws EventNotFoundException;

   // public Society retrieveSocietyById(Long societyId);

    //public Long createNewSociety(Society society);
    
}
