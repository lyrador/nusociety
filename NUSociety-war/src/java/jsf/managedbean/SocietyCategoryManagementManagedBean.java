/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import entity.SocietyCategory;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateSocietyCategoryException;
import util.exception.DeleteSocietyCategoryException;
import util.exception.SocietyCategoryNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "societyCategoryManagementManagedBean")
@SessionScoped
public class SocietyCategoryManagementManagedBean implements Serializable {

    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;

    private List<SocietyCategory> societyCategories;
    private SocietyCategory newCategory;
    private SocietyCategory categoryToUpdate;
    private SocietyCategory categoryToDelete;
    
    public SocietyCategoryManagementManagedBean() {
        newCategory = new SocietyCategory();
        categoryToUpdate = new SocietyCategory();
        categoryToDelete = new SocietyCategory();
    }
    
    @PostConstruct
    public void postConstruct() {
        this.setSocietyCategories(societyCategorySessionBeanLocal.retrieveAllSocietyCategories());
    }
    
    public void createNewSocietyCategory(ActionEvent event) {
        
        try {
            SocietyCategory category = societyCategorySessionBeanLocal.createNewSocietyCategory(newCategory);
            societyCategories.add(category);

            newCategory = new SocietyCategory();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New society category created successfully (Society Category ID: " + category.getSocietyCategoryId() + ")", null));
        } catch (CreateSocietyCategoryException | SocietyCategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new society: " + ex.getMessage(), null));

        }

    }
    
    public void updateSocietyCategory(ActionEvent event) {
        
        try {
            societyCategorySessionBeanLocal.updateSocietyCategory(categoryToUpdate);
            this.setSocietyCategories(societyCategorySessionBeanLocal.retrieveAllSocietyCategories());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New society category updated successfully", null));

        } catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        
    }
    
    public void deleteSocietyCategory(ActionEvent event) {
        
        try {
            societyCategorySessionBeanLocal.deleteSocietyCategory(categoryToDelete.getSocietyCategoryId());
            
            societyCategories.remove(newCategory);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Society Category deleted successfully", null));
        } catch(SocietyCategoryNotFoundException | DeleteSocietyCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting society category: " + ex.getMessage(), null));
        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<SocietyCategory> getSocietyCategories() {
        return societyCategories;
    }

    public void setSocietyCategories(List<SocietyCategory> societyCategories) {
        this.societyCategories = societyCategories;
    }

    public SocietyCategory getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(SocietyCategory newCategory) {
        this.newCategory = newCategory;
    }

    public SocietyCategory getCategoryToUpdate() {
        return categoryToUpdate;
    }

    public void setCategoryToUpdate(SocietyCategory categoryToUpdate) {
        this.categoryToUpdate = categoryToUpdate;
    }

    public SocietyCategory getCategoryToDelete() {
        return categoryToDelete;
    }

    public void setCategoryToDelete(SocietyCategory categoryToDelete) {
        this.categoryToDelete = categoryToDelete;
    }
    
}
