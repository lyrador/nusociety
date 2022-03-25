/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SocietySessionBeanLocal;
import entity.Society;
import entity.Student;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "societyManagedBean")
@ViewScoped
public class SocietyManagedBean implements Serializable {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    private Student student;
    private Society society;
    
    public SocietyManagedBean() {
    }
    @PostConstruct
    public void init() {
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            society = societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId));
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
}
