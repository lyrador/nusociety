/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Society;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface SocietySessionBeanLocal {

    public Society createNewSociety(Society society, List<Long> categoryIds, List<Long> staffIds) throws CreateSocietyException, UnknownPersistenceException;

    public Society retrieveSocietyById(Long societyId) throws SocietyNotFoundException;

    public List<Society> retrieveAllSocieties();

    public void updateSociety(Society newSociety, List<Long> categoryIds, List<Long> staffIds) throws SocietyNotFoundException, SocietyCategoryNotFoundException, StaffNotFoundException;

    public void deleteSociety(Long societyId) throws SocietyNotFoundException;

    public List<Society> searchSocietyByName(String searchString);

    public List<Society> retrieveSocietiesForMember(Long memberId);
    
    public void removeStudentFromSociety(Long societyId, Long studentId) throws SocietyNotFoundException, StudentNotFoundException;

    public void addStudentToSociety(Long societyId, Long studentId) throws SocietyNotFoundException, StudentNotFoundException;
    
}
