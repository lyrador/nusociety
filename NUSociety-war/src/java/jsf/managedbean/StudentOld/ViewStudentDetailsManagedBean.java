/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.StudentOld;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import util.exception.StudentNotFoundException;

/**
 *
 * @author yeeda
 */
@Named(value = "viewStudentDetailsManagedBean")
@RequestScoped
public class ViewStudentDetailsManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Long studentIdToView;
    private Student studentToView;
    
    public ViewStudentDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        studentIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("studentIdToView");
        
        try {
            if(studentIdToView != null) {
                studentToView = studentSessionBeanLocal.retrieveStudentByStudentId(studentIdToView);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No student has been selected", null));
            }
        }
        catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the student details: " + ex.getMessage(), null));
        }
        catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void phaseListener(PhaseEvent event) {        
    }
    
    public void back(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllStudents.xhtml");
    }

    public void updateStudent(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("studentIdToUpdate", studentIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateStudent.xhtml");
    }
    
    public void deleteStudent(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("studentIdToDelete", studentIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteStudent.xhtml");
    }
    
    public Student getStudentToView() {
        return studentToView;
    }

    public void setStudentToView(Student studentToView) {
        this.studentToView = studentToView;
    }
}
  
