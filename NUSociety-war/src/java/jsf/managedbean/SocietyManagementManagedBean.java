/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Announcement;
import entity.Society;
import entity.SocietyCategory;
import entity.Staff;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.exception.CreateSocietyException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author raihan
 */
@Named(value = "societyManagementManagedBean")
@SessionScoped
public class SocietyManagementManagedBean implements Serializable {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;
    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    
    private List<Society> societies;
    private List<Society> filteredSocieties;
    private List<SocietyCategory> societyCategories;
    private List<Staff> staffs;
    private List<Announcement> announcements;
    
    private Long memberId;
    private List<Society> memberSocieties;
    private List<Society> followedSocieties;
    
    private Society newSociety;
    private List<Long> newCategoryIds;
    private List<Long> newStaffIds;
    
    private Society societyToUpdate;
    private List<Long> updatedCategoryIds;
    private List<Long> updatedStaffIds;
    
    private Society societyToDelete;
    
    public SocietyManagementManagedBean() {
        newSociety = new Society();
        societyToUpdate = new Society();
        societyToDelete = new Society();
    }
    
    @PostConstruct
    public void postConstruct() {
        this.societies = societySessionBeanLocal.retrieveAllSocieties();
        this.societyCategories = societyCategorySessionBeanLocal.retrieveAllSocietyCategories();
        this.staffs = staffSessionBeanLocal.retrieveAllStaffs();
        this.announcements = announcementSessionBeanLocal.retrieveAllAnnouncements();
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            Student latestStudent = studentSessionBeanLocal.retrieveStudentByStudentId(currentStudent.getStudentId());
            this.memberId = latestStudent.getStudentId();
            this.memberSocieties = currentStudent.getMemberSocieties();
            for (Society society: currentStudent.getLeaderSocieties()) {
                if (!memberSocieties.contains(society)) {
                    memberSocieties.add(society);
                }
            }
        this.followedSocieties = currentStudent.getFollowedSocieties();
        } catch (StudentNotFoundException ex) {
            System.out.print(ex);
        }
    }
    
    public void createNewSociety(ActionEvent event) {
        
        try {
            newSociety.setDateCreated(new Date());
            Society society = societySessionBeanLocal.createNewSociety(getNewSociety(), getNewCategoryIds(), getNewStaffIds());
            getSocieties().add(society);
            
            if(filteredSocieties != null) {
                filteredSocieties.add(society);
            }
            
            setNewSociety(new Society());
            setNewCategoryIds(null);
            setNewStaffIds(null);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New society created successfully (Society ID: " + society.getSocietyId() + ")", null));

            
        } catch (CreateSocietyException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new society: " + ex.getMessage(), null));
        }
        
    }
    
    public void updateSociety(ActionEvent event) {
              
        try {
            societySessionBeanLocal.updateSociety(societyToUpdate, updatedCategoryIds, updatedStaffIds);
            
            societyToUpdate.getSocietyCategories().clear();
            
            for(SocietyCategory sc : societyCategories) {
                if(updatedCategoryIds.contains(sc.getSocietyCategoryId())) {
                    societyToUpdate.getSocietyCategories().add(sc);
                }                
            }
            
            societyToUpdate.getStaffs().clear();
            
            for(Staff s : staffs) {
                if(updatedStaffIds.contains(s.getStaffId())) {
                    societyToUpdate.getStaffs().add(s);
                }                
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Society updated successfully", null));
        } catch(SocietyNotFoundException | SocietyCategoryNotFoundException | StaffNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating society: " + ex.getMessage(), null));
        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteSociety(ActionEvent event) {
        try {
            societySessionBeanLocal.deleteSociety(societyToDelete.getSocietyId());
            
            societies.remove(societyToDelete);
            
            if(filteredSocieties != null) {
                filteredSocieties.remove(societyToDelete);
            }


            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Society deleted successfully", null));
        } catch(SocietyNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting society: " + ex.getMessage(), null));
        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void getSocietiesForMember(ActionEvent event) {
        
        try {
            memberSocieties = societySessionBeanLocal.retrieveSocietiesForMember(memberId);

        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(List<Society> societies) {
        this.societies = societies;
    }

    public List<SocietyCategory> getSocietyCategories() {
        return societyCategories;
    }

    public void setSocietyCategories(List<SocietyCategory> societyCategories) {
        this.societyCategories = societyCategories;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }

    public Society getSocietyToUpdate() {
        return societyToUpdate;
    }

    public void setSocietyToUpdate(Society societyToUpdate) {
        this.societyToUpdate = societyToUpdate;
    }

    public Society getSocietyToDelete() {
        return societyToDelete;
    }

    public void setSocietyToDelete(Society societyToDelete) {
        this.societyToDelete = societyToDelete;
    }

    public List<Long> getUpdatedCategoryIds() {
        return updatedCategoryIds;
    }

    public void setUpdatedCategoryIds(List<Long> updatedCategoryIds) {
        this.updatedCategoryIds = updatedCategoryIds;
    }

    public List<Long> getUpdatedStaffIds() {
        return updatedStaffIds;
    }

    public void setUpdatedStaffIds(List<Long> updatedStaffIds) {
        this.updatedStaffIds = updatedStaffIds;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Society getNewSociety() {
        return newSociety;
    }

    public void setNewSociety(Society newSociety) {
        this.newSociety = newSociety;
    }

    public List<Long> getNewCategoryIds() {
        return newCategoryIds;
    }

    public void setNewCategoryIds(List<Long> newCategoryIds) {
        this.newCategoryIds = newCategoryIds;
    }

    public List<Long> getNewStaffIds() {
        return newStaffIds;
    }

    public void setNewStaffIds(List<Long> newStaffIds) {
        this.newStaffIds = newStaffIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<Society> getMemberSocieties() {
        return memberSocieties;
    }

    public void setMemberSocieties(List<Society> memberSocieties) {
        this.memberSocieties = memberSocieties;
    }

    public List<Society> getFilteredSocieties() {
        return filteredSocieties;
    }

    public void setFilteredSocieties(List<Society> filteredSocieties) {
        this.filteredSocieties = filteredSocieties;
    }

    public List<Society> getFollowedSocieties() {
        return followedSocieties;
    }

    public void setFollowedSocieties(List<Society> followedSocieties) {
        this.followedSocieties = followedSocieties;
    }
    
    
}
