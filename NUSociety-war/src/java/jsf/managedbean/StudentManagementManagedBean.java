/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
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
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "studentManagementManagedBean")
@ViewScoped
public class StudentManagementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private List<Student> students;
    private Student newStudent;
    private Student studentToUpdate;
    private Student studentToDelete;
    private List<Student> filteredStudents;
    
    public StudentManagementManagedBean() {
        newStudent = new Student();
        studentToUpdate = new Student();
        studentToDelete = new Student();
    }
    
    @PostConstruct
    public void postConstruct() {
        this.students = studentSessionBeanLocal.retrieveAllStudents();
    }
    
    public void createNewStudent(ActionEvent event) {
        Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);      
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New student created: " + newStudentId,"New student created: " + newStudentId));          
    }
    
    public void updateStudent(ActionEvent event) throws StudentNotFoundException {
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

    public List<Student> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(List<Student> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }
}