/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Notification;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author yeeda
 */
@Stateless
public class StudentSessionBean implements StudentSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Student> retrieveAllStudents() {
        Query query = em.createQuery("SELECT s FROM Student s");     
        return query.getResultList();
    }
    
    @Override
    public Student retrieveStudentByStudentId(Long studentId) {
        Student student = em.find(Student.class, studentId);     
        return student;
    }
    
    @Override
    public Long createNewStudent(Student newStudent) {
        em.persist(newStudent);
        em.flush(); 
        return newStudent.getStudentId();
    }
    
    @Override
    public void deleteStudent(Long studentId) {
        Student studentToBeDeleted = retrieveStudentByStudentId(studentId);
        
        for(Attendance attendance: studentToBeDeleted.getAttendances()) {
            em.remove(attendance);
        }       
        studentToBeDeleted.getAttendances().clear();
        
        for(Notification notification: studentToBeDeleted.getNotifications()) {
            
            notification.getStudents().remove(studentToBeDeleted);            
            if (notification.getStudents().size() == 0) {                  
                em.remove(notification);
            }
        }       
        studentToBeDeleted.getNotifications().clear();
        
        em.remove(studentToBeDeleted); 
    }
}
