/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.NotificationSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Notification;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.NotificationNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "notificationManagementManagedBean")
@ViewScoped
public class NotificationManagementManagedBean implements Serializable {

    @EJB
    private NotificationSessionBeanLocal notificationSessionBeanLocal;
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private List<Notification> notifications;
    private Notification newNotification;
    private Notification notificationToUpdate;
    private Notification notificationToDelete;
    
    private List<Long> existingStudentIds;
    private List<Student> existingStudents;
    
    public NotificationManagementManagedBean() {
        newNotification = new Notification();
        notificationToUpdate = new Notification();
        notificationToDelete = new Notification();
    }
    
    @PostConstruct
    public void postConstruct() {
        existingStudentIds = null;
        this.existingStudents = studentSessionBeanLocal.retrieveAllStudents();
        this.notifications = notificationSessionBeanLocal.retrieveAllNotifications();
    }
    
    public void createNewNotification(ActionEvent event) throws StudentNotFoundException {
        newNotification.setCreationDate(new Date());
        if (existingStudentIds.size() > 0) {
            for (int i = 0; i < existingStudentIds.size(); i++) {
                System.out.println("HELLOOOOOO");            
                Student studentToUpdate = studentSessionBeanLocal.retrieveStudentByStudentId(existingStudentIds.get(i));
                System.out.println(studentToUpdate.getName());
                newNotification.getStudents().add(studentToUpdate);
                System.out.println(studentToUpdate.getName());
                studentToUpdate.getNotifications().add(newNotification);
                System.out.println(studentToUpdate.getName());
            }
            Long newNotificationId = notificationSessionBeanLocal.createNewNotification(newNotification);      
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New notification created: " + newNotificationId,"New notification created: " + newNotificationId));          
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! No students selected.", null));
        }
    }
    
    public void updateNotification(ActionEvent event) throws NotificationNotFoundException {
        notificationSessionBeanLocal.updateNotification(notificationToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification updated: " + notificationToUpdate,"Notification updated: " + notificationToUpdate));
    }
    
    public void deleteNotification(ActionEvent event) {
        try {
            notificationSessionBeanLocal.deleteNotification(notificationToDelete.getNotificationId());  
            notifications.remove(notificationToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification deleted successfully", null));
        } 
        catch(NotificationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting notification: " + ex.getMessage(), null));
        }
        catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Notification getNotificationToUpdate() {
        return notificationToUpdate;
    }

    public void setNotificationToUpdate(Notification notificationToUpdate) {
        this.notificationToUpdate = notificationToUpdate;
    }

    public Notification getNotificationToDelete() {
        return notificationToDelete;
    }

    public void setNotificationToDelete(Notification notificationToDelete) {
        this.notificationToDelete = notificationToDelete;
    }

    public Notification getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Notification newNotification) {
        this.newNotification = newNotification;
    }  

    public List<Student> getExistingStudents() {
        return existingStudents;
    }

    public void setExistingStudents(List<Student> existingStudents) {
        this.existingStudents = existingStudents;
    }

    public List<Long> getExistingStudentIds() {
        return existingStudentIds;
    }

    public void setExistingStudentIds(List<Long> existingStudentIds) {
        this.existingStudentIds = existingStudentIds;
    } 
}