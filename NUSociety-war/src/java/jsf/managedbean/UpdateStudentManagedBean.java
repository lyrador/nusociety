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
 * @author yeeda
 */
@Named(value = "updateStudentManagedBean")
@RequestScoped
public class UpdateStudentManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Student studentToBeUpdated;

    /**
     * Creates a new instance of UpdateStudentManagedBean
     */
    public UpdateStudentManagedBean() {
        studentToBeUpdated = new Student();
    }
    
    public void doUpdateStudent(ActionEvent event) throws StudentNotFoundException{
        studentSessionBeanLocal.updateStudent(studentToBeUpdated);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student updated: " + studentToBeUpdated,"Student updated: " + studentToBeUpdated));
    }
    
    public Student getStudent() {
        return studentToBeUpdated;
    }
}
  
