/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Notification;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NotificationNotFoundException;

/**
 *
 * @author yeeda
 */
@Stateless
public class NotificationSessionBean implements NotificationSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Notification> retrieveAllNotifications() {
        Query query = em.createQuery("SELECT n FROM Notification n");     
        return query.getResultList();
    }
    
    @Override
    public Notification retrieveNotificationByNotificationId(Long notificationId) throws NotificationNotFoundException {
        Notification notification = em.find(Notification.class, notificationId);
        
        if (notification != null) {
            return notification;
        } else {
            throw new NotificationNotFoundException("Notification with Id: " + notificationId + " cannot be found!");
        }
    }
    
    @Override
    public Long createNewNotification(Notification newNotification) {
        em.persist(newNotification);
        em.flush(); 
        return newNotification.getNotificationId();
    }
    
    @Override
    public void deleteNotification(Long notificationId) throws NotificationNotFoundException{
        Notification notificationToBeDeleted = retrieveNotificationByNotificationId(notificationId);
        
        for(Student student: notificationToBeDeleted.getStudents()) {
            em.remove(student);
        }       
        notificationToBeDeleted.getStudents().clear();
        
        em.remove(notificationToBeDeleted); 
    }
    
    @Override
    public void updateNotification(Notification tempNotification) throws NotificationNotFoundException{
        
        Notification notificationToUpdate = em.find(Notification.class, tempNotification.getNotificationId());

        if (tempNotification.getContent()!= null)
            notificationToUpdate.setContent(tempNotification.getContent());
    }
}
