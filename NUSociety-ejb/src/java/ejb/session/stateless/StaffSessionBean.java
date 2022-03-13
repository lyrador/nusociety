/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Society;
import entity.Staff;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.StaffDeletionException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author 65912
 */
@Stateless
public class StaffSessionBean implements StaffSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewStaff(Staff staff) {
        em.persist(staff);
        em.flush();
        return staff.getStaffId();
    }
    
    @Override
    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException {
        Staff s = em.find(Staff.class, staffId);
        
        if(staffId != null) {
            return s; 
        } else {
            throw new StaffNotFoundException("Error, Staff with ID:" + staffId + " not found!");
        }      
    }
    
    //Update staff
    @Override
    public void updateStaff(Staff newS) throws StaffNotFoundException {
        Staff s = retrieveStaffById(newS.getStaffId());
        
        s.setEmail(newS.getEmail());
        s.setPassword(newS.getPassword());
        s.setProfilePicture(newS.getProfilePicture());
        s.setUserName(newS.getUserName());
    }
    
    //Delete staff
    //**Staff should only be able to be deleted if their society have more than one Staff || Staff not in charge of society**
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException, StaffDeletionException{
        Staff s = retrieveStaffById(staffId);
        boolean enoughStaff = false;
        
        //Check if the Societies that the Staff is in charge of has at least 1 Staff member after deletion
        for(Society so : s.getSocieties()) {
            if(so.getStaffs().size() >= 2) {
                enoughStaff = false;
            }
        }
        
        if(s.getSocieties().isEmpty() || enoughStaff) {
            em.remove(s);
        } else {
            throw new StaffDeletionException("Staff with ID: " + staffId +
                    " is in charge of a Society. Unable to delete unless another Staff takes charge of the Society!");
                    
        }
    }
     
    @Override
    public Staff retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM Staff s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Staff) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Error, Employee with username " + username + " does not exist.");
        }
    }
    
    @Override
    public List<Staff> retrieveAllStaffs() {
        Query query = em.createQuery("SELECT s FROM Staff s");

        return query.getResultList();
    }
    
}
