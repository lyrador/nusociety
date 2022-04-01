/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EventCategory;
import java.util.List;
import javax.ejb.Local;
import util.exception.EventCategoryAlreadyExistsException;
import util.exception.EventCategoryNotFoundException;

/**
 *
 * @author ajayan
 */
@Local
public interface EventCategorySessionBeanLocal {

    public void deleteEventCategory(Long eventCategoryId) throws EventCategoryNotFoundException;

    public Long createNewEventCategory(EventCategory eventCategory);

    public EventCategory retrieveEventCategoryByName(String eventCategoryName) throws EventCategoryNotFoundException;

    public EventCategory retrieveEventCategoryById(Long eventCategoryId) throws EventCategoryNotFoundException;

    public List<EventCategory> retrieveAllEvents();

    public void updateEventCategory(EventCategory eventCategory, Long eventCategoryId) throws EventCategoryNotFoundException;

    
}
