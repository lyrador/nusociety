/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.AttendanceSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.AttendanceNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "rosterManagementManagedBean")
@ViewScoped
public class RosterManagementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;
    
    private List<Student> students;
    private Student newStudent;
    private Student studentToUpdate;
    private Student studentToDelete;
    private Society currentSociety;
    private Attendance tempAttendance;
    private Attendance viewedAttendance;
    private HashMap<Long, Attendance> attendances;
    private String currentValue;
    
    public RosterManagementManagedBean() {
        newStudent = new Student();
        studentToUpdate = new Student();
        studentToDelete = new Student();
    }
    
    @PostConstruct
    public void postConstruct() {  
        Long currentSocietyId = (long)1;
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSocietyId);
        System.out.println("HELLO WORLD");
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSocietyId);
        System.out.println("HELLO WORLD 22");
    }
    
    public void createNewStudent(ActionEvent event) {
        Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);      
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New student created: " + newStudentId,"New student created: " + newStudentId));          
    }
    
    public void updateStudent(ActionEvent event) throws StudentNotFoundException {
//        Student existingStudent = studentSessionBeanLocal.retrieveStudentByStudentId(studentToUpdate.getStudentId());
//        if (studentToUpdate.getName() == null) {
//            studentToUpdate.setName(existingStudent.getName());
//        }
//        if (studentToUpdate.getEmail() == null) {
//            studentToUpdate.setEmail(existingStudent.getEmail());
//        }
//        if (studentToUpdate.getPassword() == null) {
//            studentToUpdate.setPassword(existingStudent.getPassword());
//        }
//        if (studentToUpdate.getUserName()== null) {
//            studentToUpdate.setUserName(existingStudent.getUserName());
//        }
//        if (studentToUpdate.getProfilePicture()== null) {
//            studentToUpdate.setProfilePicture(existingStudent.getProfilePicture());
//        }
//        if (studentToUpdate.getAccessRightEnum()== null) {
//            studentToUpdate.setAccessRightEnum(existingStudent.getAccessRightEnum());
//        }
        studentSessionBeanLocal.updateStudent(studentToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student updated: " + studentToUpdate,"Student updated: " + studentToUpdate));
    }
    
    public void deleteStudent(ActionEvent event) {
        try {
            studentSessionBeanLocal.deleteStudent(studentToDelete.getStudentId());  
            students.remove(studentToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student deleted successfully", null));
        } 
        catch(StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting student: " + ex.getMessage(), null));
        }
        catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void removeStudent(ActionEvent event) {
        System.out.println("IOGFHEQPIG EPW");
        try {
            Student studentToRemove = (Student) event.getComponent().getAttributes().get("studentToRemove");
            Society currentSociety = societySessionBeanLocal.retrieveSocietyById((long) 1);
            societySessionBeanLocal.removeStudentFromSociety(currentSociety.getSocietyId(), studentToRemove.getStudentId());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student removed successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void setAttendance(ActionEvent event) {
        System.out.println("UPDATE ATTENDANCE EVENT PT 1");
        try {
            Student tempStudentToUpdate = (Student) event.getComponent().getAttributes().get("studentToUpdate");
            System.out.println(tempStudentToUpdate.getEmail());
            Society currentSociety = societySessionBeanLocal.retrieveSocietyById((long) 1);
            tempAttendance = attendanceSessionBeanLocal.retrieveAttendanceFromStudentIdAndSocietyId(tempStudentToUpdate.getStudentId(), currentSociety.getSocietyId());
            currentValue = tempAttendance.getAttendedCount() + " / " + tempAttendance.getTotalCount() + " (" + tempAttendance.getAttendanceRate() + "%)";
     
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Temp attendance updated successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void updateAttendance(ActionEvent event) throws AttendanceNotFoundException {
        attendanceSessionBeanLocal.updateAttendance(tempAttendance);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Attendance updated: " + tempAttendance,"Attendance updated: " + tempAttendance));
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Student getStudentToUpdate() {
        return studentToUpdate;
    }

    public void setStudentToUpdate(Student studentToUpdate) {
        this.studentToUpdate = studentToUpdate;
    }

    public Student getStudentToDelete() {
        return studentToDelete;
    }

    public void setStudentToDelete(Student studentToDelete) {
        this.studentToDelete = studentToDelete;
    }

    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }  

    public Attendance getTempAttendance() {
        return tempAttendance;
    }

    public void setTempAttendance(Attendance tempAttendance) {
        this.tempAttendance = tempAttendance;
    }

    public HashMap<Long, Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(HashMap<Long, Attendance> attendances) {
        this.attendances = attendances;
    } 

    public Attendance getViewedAttendance() {
        return viewedAttendance;
    }

    public void setViewedAttendance(Attendance viewedAttendance) {
        this.viewedAttendance = viewedAttendance;
    }
    
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}