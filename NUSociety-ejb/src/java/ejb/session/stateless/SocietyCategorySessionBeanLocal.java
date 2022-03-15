/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SocietyCategory;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateSocietyCategoryException;
import util.exception.DeleteSocietyCategoryException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.UpdateSocietyCategoryException;

/**
 *
 * @author raihan
 */
@Local
public interface SocietyCategorySessionBeanLocal {

    public SocietyCategory createNewSocietyCategory(SocietyCategory societyCategory) throws CreateSocietyCategoryException, SocietyCategoryNotFoundException;

    public SocietyCategory retrieveSocietyCategoryById(Long id) throws SocietyCategoryNotFoundException;

    public List<SocietyCategory> retrieveAllSocietyCategories();

    public void updateSocietyCategory(SocietyCategory category) throws SocietyCategoryNotFoundException, UpdateSocietyCategoryException;

    public void deleteSocietyCategory(Long categoryId) throws SocietyCategoryNotFoundException, DeleteSocietyCategoryException;

    public SocietyCategory retrieveSocietyCategoryByName(String name) throws SocietyCategoryNotFoundException;
    
}
