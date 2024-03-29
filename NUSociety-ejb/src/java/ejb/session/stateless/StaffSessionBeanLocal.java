/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffDeletionException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author 65912
 */
@Local
public interface StaffSessionBeanLocal {

    public Long createNewStaff(Staff staff);

    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException;

    public Staff retrieveStaffByUsername(String username) throws StaffNotFoundException;

    public void updateStaff(Staff newS) throws StaffNotFoundException;

    public void deleteStaff(Long staffId) throws StaffNotFoundException, StaffDeletionException;

    public List<Staff> retrieveAllStaffs();

    public Staff staffLogin(String username, String password) throws InvalidLoginCredentialException, StaffNotFoundException;
    
}
