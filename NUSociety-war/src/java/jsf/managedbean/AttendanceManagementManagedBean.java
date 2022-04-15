/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AttendanceSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.AttendanceNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "attendanceManagementManagedBean")
@ViewScoped
public class AttendanceManagementManagedBean implements Serializable {

    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private List<Attendance> attendances;
    private Attendance newAttendance;
    private Attendance attendanceToUpdate;
    private Attendance attendanceToDelete;
    
    private Student existingStudent;
    
    public AttendanceManagementManagedBean() {
        newAttendance = new Attendance();
        attendanceToUpdate = new Attendance();
        attendanceToDelete = new Attendance();
        existingStudent = new Student();
    }
    
    @PostConstruct
    public void postConstruct() {
        this.attendances = attendanceSessionBeanLocal.retrieveAllAttendances();
    }
    
    public void createNewAttendance(ActionEvent event) throws StudentNotFoundException, SocietyNotFoundException {
        Student studentToUpdate = studentSessionBeanLocal.retrieveStudentByStudentId(getExistingStudent().getStudentId());
        newAttendance.setStudent(studentToUpdate);
        Long newAttendanceId = attendanceSessionBeanLocal.createNewAttendance(newAttendance);      
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New attendance created: " + newAttendanceId,"New attendance created: " + newAttendanceId));          
    }
    
    public void updateAttendance(ActionEvent event) throws AttendanceNotFoundException {
        attendanceSessionBeanLocal.updateAttendance(attendanceToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Attendance updated: " + attendanceToUpdate,"Attendance updated: " + attendanceToUpdate));
    }
    
    public void deleteAttendance(ActionEvent event) {
        try {
            attendanceSessionBeanLocal.deleteAttendance(attendanceToDelete.getAttendanceId());  
            attendances.remove(attendanceToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Attendance deleted successfully", null));
        } 
        catch(AttendanceNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting attendance: " + ex.getMessage(), null));
        }
        catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance getAttendanceToUpdate() {
        return attendanceToUpdate;
    }

    public void setAttendanceToUpdate(Attendance attendanceToUpdate) {
        this.attendanceToUpdate = attendanceToUpdate;
    }

    public Attendance getAttendanceToDelete() {
        return attendanceToDelete;
    }

    public void setAttendanceToDelete(Attendance attendanceToDelete) {
        this.attendanceToDelete = attendanceToDelete;
    }

    public Attendance getNewAttendance() {
        return newAttendance;
    }

    public void setNewAttendance(Attendance newAttendance) {
        this.newAttendance = newAttendance;
    }  
    
    public Student getExistingStudent() {
        return existingStudent;
    }

    public void setExistingStudent(Student existingStudent) {
        this.existingStudent = existingStudent;
    }
}