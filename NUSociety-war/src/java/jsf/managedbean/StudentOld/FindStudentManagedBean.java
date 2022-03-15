/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.StudentOld;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
import java.io.IOException;
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
@Named(value = "findStudentManagedBean")
@RequestScoped
public class FindStudentManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Student studentToFind;

    /**
     * Creates a new instance of DeleteStudentManagedBean
     */
    public FindStudentManagedBean() {
        studentToFind = new Student();
    }
    
    public void doViewStudentDetails(ActionEvent event) throws StudentNotFoundException, IOException {
        Long studentToFindId = studentToFind.getStudentId();
        studentToFind = studentSessionBeanLocal.retrieveStudentByStudentId(studentToFindId);
        
        System.out.println(studentToFind.getName());

        Long studentIdToView = (Long)event.getComponent().getAttributes().get("studentId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("studentIdToView", studentIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewStudentDetailsManagedBean.xhtml");
        
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student viewed: " + studentToFindId,"Student viewed: " + studentToFindId));
    }
    
    public Student getStudent() {
        return studentToFind;
    }
}
  
