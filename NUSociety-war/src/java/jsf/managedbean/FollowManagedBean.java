/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Society;
import entity.Student;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "followManagedBean")
@ViewScoped
public class FollowManagedBean implements Serializable {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    private Student currentStudent;
    private Long societyId; 
    private List<Society> followedSocieties;
    private List<Society> memberSocieties;
    
    private String followerCount;
    private String memberCount;
    
    public FollowManagedBean() {    
    }
    
    @PostConstruct
    public void postConstruct() {
        
        try {
            this.currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
            this.societyId = Long.valueOf(id);
            Society society = societySessionBeanLocal.retrieveSocietyById(societyId);
            this.followedSocieties = currentStudent.getFollowedSocieties();
            this.memberSocieties = currentStudent.getMemberSocieties();
            
            this.followerCount = String.valueOf(society.getFollowedStudents().size());
            this.memberCount = String.valueOf(society.getMemberStudents().size());
        } 
        catch(SocietyNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to follow: " + ex.getMessage(), null));
        }
    }
    
    public void follow(ActionEvent e) {
        try {
            studentSessionBeanLocal.studentFollow(currentStudent.getStudentId(), societyId);
            Society society = societySessionBeanLocal.retrieveSocietyById(societyId);
            this.followedSocieties.add(society);
            int followers = Integer.valueOf(followerCount);
            followers += 1;
            followerCount = String.valueOf(followers);
        }
        catch(StudentNotFoundException | SocietyNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to follow: " + ex.getMessage(), null));
        }
    }
    
    public void unfollow(ActionEvent e) {
        try {
            System.out.println("I am being executed!");
            studentSessionBeanLocal.studentUnfollow(currentStudent.getStudentId(), societyId);
            Society society = societySessionBeanLocal.retrieveSocietyById(societyId);
            this.followedSocieties.remove(society);
            int followers = Integer.valueOf(followerCount);
            followers -= 1;
            followerCount = String.valueOf(followers);
        }
        catch(StudentNotFoundException | SocietyNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to follow: " + ex.getMessage(), null));
        }
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public List<Society> getFollowedSocieties() {
        return followedSocieties;
    }

    public void setFollowedSocieties(List<Society> followedSocieties) {
        this.followedSocieties = followedSocieties;
    }

    public List<Society> getMemberSocieties() {
        return memberSocieties;
    }

    public void setMemberSocieties(List<Society> memberSocieties) {
        this.memberSocieties = memberSocieties;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    
}
