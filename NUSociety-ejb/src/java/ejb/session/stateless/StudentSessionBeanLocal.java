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

/**
 *
 * @author yeeda
 */
@Local
public interface StudentSessionBeanLocal {

    public List<Student> retrieveAllStudents();

    public Long createNewStudent(Student newStudent);

    public void deleteStudent(Long studentId);

    public Student retrieveStudentByStudentId(Long studentId);
    
}
