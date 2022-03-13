/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Society;
import entity.SocietyCategory;
import entity.Staff;
import entity.Student;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateSocietyException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author raihan
 */
@Stateless
public class SocietySessionBean implements SocietySessionBeanLocal {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;
    
    @Override
    public Society createNewSociety(Society society, List<Long> categoryIds, List<Long> staffIds) throws CreateSocietyException, UnknownPersistenceException {
        
        try {
            if (categoryIds.isEmpty()) {
                throw new CreateSocietyException("The new society must be associated with at least one society category");
            } else if (staffIds.isEmpty()) {
                throw new CreateSocietyException("The new society must be associated with at least one staff");
            } else {
                
                for (Long id : categoryIds) {
                    SocietyCategory category = societyCategorySessionBeanLocal.retrieveSocietyCategoryById(id);
                    society.getSocietyCategories().add(category);
                }
                
                for (Long id : staffIds) {
                    Staff staff = staffSessionBeanLocal.retrieveStaffById(id);
                    society.getStaffs().add(staff);
                }
                
                em.persist(society);
                em.flush();
                
                return society;
            }
            
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        } catch (SocietyCategoryNotFoundException | StaffNotFoundException ex) {
            throw new CreateSocietyException("An error has occurred while creating the new product: " + ex.getMessage());
        }
        
    }
    
    @Override
    public Society retrieveSocietyById(Long societyId) throws SocietyNotFoundException {
        Society society = em.find(Society.class, societyId);
        
        try {
            return society;
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new SocietyNotFoundException("Society Id " + societyId + " does not exist!");
        }
    }
    
    @Override
    public List<Society> retrieveAllSocieties() {
        Query query = em.createQuery("SELECT s FROM Society s");
        List<Society> societies = query.getResultList();
        
        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
        }
        return query.getResultList();
    }
    
    @Override
    public void updateSociety(Society newSociety, List<Long> categoryIds, List<Long> staffIds) throws SocietyNotFoundException, SocietyCategoryNotFoundException, StaffNotFoundException {
        
        Society societyToUpdate = retrieveSocietyById(newSociety.getSocietyId());
        
        if (societyToUpdate.getSocietyId().equals(newSociety.getSocietyId())) {
            
            //Update categories
            for (SocietyCategory category : newSociety.getSocietyCategories()) {
                category.getSocieties().remove(societyToUpdate);
            }
            societyToUpdate.getSocietyCategories().clear();
            
            for (Long id : categoryIds) {
                SocietyCategory category = societyCategorySessionBeanLocal.retrieveSocietyCategoryById(id);
                societyToUpdate.getSocietyCategories().add(category);
            }
            
            //Update staffs
            for (Staff staff : newSociety.getStaffs()) {
                staff.getSocieties().remove(societyToUpdate);
            }
            societyToUpdate.getStaffs().clear();
            
            for (Long id : staffIds) {
                Staff staff = staffSessionBeanLocal.retrieveStaffById(id);
                societyToUpdate.getStaffs().add(staff);
            }
            
            //Update everything else
            societyToUpdate.setName(newSociety.getName());
            societyToUpdate.setDescription(newSociety.getDescription());
            societyToUpdate.setProfilePicture(newSociety.getProfilePicture());
        }
        
    }
    
    @Override
    public void deleteSociety(Long societyId) throws SocietyNotFoundException {
        Society societyToDelete = retrieveSocietyById(societyId);
        
        //Announcement
        if (!societyToDelete.getAnnouncements().isEmpty()) {
            //Call announcement session bean to delete announcements
            societyToDelete.getAnnouncements().clear();
        }
        
        //SocietyCategory
        for (SocietyCategory category : societyToDelete.getSocietyCategories()) {
            category.getSocieties().remove(societyToDelete);
        }
        
        //Staff
        for (Staff staff : societyToDelete.getStaffs()) {
            staff.getSocieties().remove(societyToDelete);
        }
        
        //Post
        if (!societyToDelete.getPosts().isEmpty()) {
            //Call post session bean to delete posts
            societyToDelete.getPosts().clear();
        }
        
        
        //Student
        for (Student student : societyToDelete.getStudents()) {
//            student.getSocieties().remove(societyToDelete);
        }
        societyToDelete.getStudents().clear();
        
        //Event
//        if (!societyToDelete.getEvents().isEmpty()) {
//            //Call event session bean to delete events
//            societyToDelete.getEvents().clear();
//        }

        em.remove(societyToDelete);
    }
    
    @Override
    public List<Society> searchSocietyByName(String searchString) {
        Query query = em.createQuery("SELECT s FROM Society s WHERE s.name LIKE :inSearchString ORDER BY s.societyId ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<Society> societies = query.getResultList();
        
        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
        }
        return query.getResultList();
    }
    

}
