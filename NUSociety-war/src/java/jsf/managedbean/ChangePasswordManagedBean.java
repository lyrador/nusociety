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
@Named(value = "changePasswordManagedBean")
@RequestScoped
public class ChangePasswordManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    private Student currentStudent;
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
    
    public ChangePasswordManagedBean() {
        this.currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
    }
    
    public void changePassword(ActionEvent event) {
        
        try {
            if (oldPassword.equals(currentStudent.getPassword())) {
                if (newPassword1.equals(newPassword2)) {
                    currentStudent.setPassword(newPassword1);
                    studentSessionBeanLocal.updateStudent(currentStudent);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "Password updated successfully!", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "New passwords do not match!", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "Old password is incorrect!", ""));
            }
        }
        catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  ex.getMessage(), ""));
        }
        
        
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
    
}
