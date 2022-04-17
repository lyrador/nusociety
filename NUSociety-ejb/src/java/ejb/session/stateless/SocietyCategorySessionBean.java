/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SocietyCategory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateSocietyCategoryException;
import util.exception.DeleteSocietyCategoryException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.UpdateSocietyCategoryException;

/**
 *
 * @author raihan
 */
@Stateless
public class SocietyCategorySessionBean implements SocietyCategorySessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public SocietyCategory createNewSocietyCategory(SocietyCategory societyCategory) throws CreateSocietyCategoryException, SocietyCategoryNotFoundException {
        
        Query query = em.createQuery("SELECT c FROM SocietyCategory c WHERE c.societyCategoryName = :inName");
        query.setParameter("inName", societyCategory.getSocietyCategoryName());
        List<SocietyCategory> list = query.getResultList();
        
        if (list.isEmpty()) {
            em.persist(societyCategory);
            em.flush();
        } else {
            throw new CreateSocietyCategoryException("Society Category already exists!");
        }

        
        return societyCategory;
    }
    
    @Override
    public SocietyCategory retrieveSocietyCategoryById(Long id) throws SocietyCategoryNotFoundException {
        SocietyCategory category = em.find(SocietyCategory.class, id);
        
        if (category != null) {
            return category;
        } else {
            throw new SocietyCategoryNotFoundException("Society Category Id " + id + " does not exist!");
        }
    }
    
    @Override
    public SocietyCategory retrieveSocietyCategoryByName(String name) throws SocietyCategoryNotFoundException {
        Query query = em.createQuery("SELECT c FROM SocietyCategory c WHERE c.societyCategoryName = :inName");
        query.setParameter("inName", name);
        
        SocietyCategory category = (SocietyCategory) query.getSingleResult();
        
        if (category != null) {
            return category;
        } else {
            throw new SocietyCategoryNotFoundException("Society Category Name " + name + " does not exist!");
        }
    }
    
    @Override
    public List<SocietyCategory> retrieveAllSocietyCategories() {
        Query query = em.createQuery("SELECT c FROM SocietyCategory c");
        List<SocietyCategory> categories = query.getResultList();
        
        for (SocietyCategory category : categories) {
            category.getSocieties().size();
        }
        
        return categories;
    }
    
    @Override
    public void updateSocietyCategory(SocietyCategory category) throws SocietyCategoryNotFoundException, UpdateSocietyCategoryException {
        
        if (category.getSocietyCategoryId() != null) {
            SocietyCategory categoryToUpdate = retrieveSocietyCategoryById(category.getSocietyCategoryId());
            
            Query query = em.createQuery("SELECT c FROM SocietyCategory c WHERE c.societyCategoryName = :inName AND c.societyCategoryId <> :inId");
            query.setParameter("inName", category.getSocietyCategoryName());
            query.setParameter("inId", category.getSocietyCategoryId());
            
            if(!query.getResultList().isEmpty()) {
                throw new UpdateSocietyCategoryException("The name of the society category to be updated is duplicated");
            }
            
            categoryToUpdate.setSocietyCategoryName(category.getSocietyCategoryName());
        } else {
            throw new SocietyCategoryNotFoundException("Society Category Id " + category.getSocietyCategoryId() + " does not exist!");
        }
    }
    
    @Override
    public void deleteSocietyCategory(Long categoryId) throws SocietyCategoryNotFoundException, DeleteSocietyCategoryException {
        
        SocietyCategory categoryToDelete = retrieveSocietyCategoryById(categoryId);
        categoryToDelete.getSocieties().size();
        if(!categoryToDelete.getSocieties().isEmpty()) {
            throw new DeleteSocietyCategoryException("Tag ID " + categoryId + " is associated with existing products and cannot be deleted!");
        } else {
            em.remove(categoryToDelete);
        }  
    }
}
