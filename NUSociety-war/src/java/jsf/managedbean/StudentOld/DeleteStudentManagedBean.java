/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.StudentOld;

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
@Named(value = "deleteStudentManagedBean")
@RequestScoped
public class DeleteStudentManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Student studentToBeDeleted;

    /**
     * Creates a new instance of DeleteStudentManagedBean
     */
    public DeleteStudentManagedBean() {
        studentToBeDeleted = new Student();
    }
    
    public void doDeleteStudent(ActionEvent event) throws StudentNotFoundException {
        Long studentToBeDeletedId = studentToBeDeleted.getStudentId();
        studentSessionBeanLocal.deleteStudent(studentToBeDeletedId);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student deleted: " + studentToBeDeletedId,"Student deleted: " + studentToBeDeletedId));
    }
    
    public Student getStudent() {
        return studentToBeDeleted;
    }
}
  
