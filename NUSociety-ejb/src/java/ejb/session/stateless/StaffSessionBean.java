/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Staff retrievePostById(Long staffId) throws StaffNotFoundException {
        Staff s = em.find(Staff.class, staffId);
        
        if(staffId != null) {
            return s; 
        } else {
            throw new StaffNotFoundException("Error, Staff with ID:" + staffId + " not found!");
        }      
    }
    
    //Update staff
    
    //Delete staff
    //**Staff should only be able to be deleted if their society have more than one Staff**
     
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
    
}
