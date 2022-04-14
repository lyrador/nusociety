/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Student;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author yeeda
 */
@Local
public interface StudentSessionBeanLocal {

    public List<Student> retrieveAllStudents();

    public Student retrieveStudentByStudentId(Long studentId) throws StudentNotFoundException;

    public Long createNewStudent(Student newStudent);

    public void deleteStudent(Long studentId) throws StudentNotFoundException;

    public void updateStudent(Student tempStudent) throws StudentNotFoundException;

    public Student retrieveStudentByUsername(String username) throws StudentNotFoundException;

    public Student studentLogin(String username, String password) throws InvalidLoginCredentialException, StudentNotFoundException;
    
    public List<Student> retrieveAllStudentsFromSocietyId(Long societyId);

    public Student studentFollow(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException;

    public Student studentUnfollow(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException;

    public Student retrieveStudentByEmail(String email) throws StudentNotFoundException;

    public void setStudentLeaderOfSociety(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException;

    public Long createNewStudentWithListOfSocietyIdsToBeLeaderOf(Student newStudent, List<Long> listOfSocietyIdsToBeLeaderOf) throws StudentNotFoundException, SocietyNotFoundException;
  
}
