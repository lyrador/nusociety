/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EventSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import entity.Event;
import entity.Society;
import entity.SocietyCategory;
import entity.Staff;
import entity.Student;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.EventNotFoundException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "editSocietyManagedBean")
@ViewScoped
public class EditSocietyManagedBean implements Serializable{

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    private Student student;
    private Society society; 
    
    public EditSocietyManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        System.out.println("*****" + societyId);
        try {
            society = societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId));
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
    }
    
    public void editSociety(ActionEvent e) {
        try {
            List<Long> categoryIds = new ArrayList<>();
            for (SocietyCategory c : society.getSocietyCategories()) {
                categoryIds.add(c.getSocietyCategoryId());
            }
            
            List<Long> staffIds = new ArrayList<>();
            for (Staff s : society.getStaffs()) {
                staffIds.add(s.getStaffId());
            }
            
            societySessionBeanLocal.updateSociety(society, categoryIds, staffIds);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Society updated successfully! ", null));
        } catch (SocietyNotFoundException | SocietyCategoryNotFoundException | StaffNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Society: " + ex.getMessage(), null));

        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    } 
}
