/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.StudentNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "editProfileManagedBean")
@RequestScoped
public class EditProfileManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    private Student currentStudent;

    public EditProfileManagedBean() {
        this.currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
    }
    
    public void updateProfile(ActionEvent event) {
        
        try {
            studentSessionBeanLocal.updateStudent(currentStudent);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile of student (Student ID: " + currentStudent.getStudentId() + ") updated!", null));

        } catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the student: " + currentStudent.getStudentId(), null));

        }
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }
    
}
