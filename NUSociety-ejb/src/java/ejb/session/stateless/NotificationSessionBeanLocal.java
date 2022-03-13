/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Notification;
import entity.Student;
import java.util.List;
import javax.ejb.Local;
import util.exception.NotificationNotFoundException;

/**
 *
 * @author yeeda
 */
@Local
public interface NotificationSessionBeanLocal {

    public List<Notification> retrieveAllNotifications();

    public Notification retrieveNotificationByNotificationId(Long notificationId) throws NotificationNotFoundException;

    public Long createNewNotification(Notification newNotification);

    public void deleteNotification(Long notificationId) throws NotificationNotFoundException;

    public void updateNotification(Notification tempNotification) throws NotificationNotFoundException;

    
}
