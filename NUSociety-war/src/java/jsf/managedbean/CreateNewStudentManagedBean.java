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

/**
 *
 * @author yeeda
 */
@Named(value = "createNewStudentManagedBean")
@RequestScoped
public class CreateNewStudentManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Student newStudent;

    /**
     * Creates a new instance of CreateNewStudentManagedBean
     */
    public CreateNewStudentManagedBean() {
        newStudent = new Student();
    }
    
    public void doCreateNewStudent(ActionEvent event) {
        Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New student created: " + newStudentId,"New student created: " + newStudentId));
    }
    
    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }
}
  
