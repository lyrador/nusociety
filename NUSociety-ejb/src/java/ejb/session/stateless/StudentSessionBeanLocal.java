/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Student;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.AccessRightEnum;
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

    public List<Student> retrieveAllStudentsFromSocietyId(Long societyId);
  
}
