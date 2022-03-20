/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Comment;
import entity.Event;
import entity.Notification;
import entity.Post;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.AccessRightEnum;
import util.exception.StudentNotFoundException;

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
    public Student retrieveStudentByStudentId(Long studentId) throws StudentNotFoundException{
        Student student = em.find(Student.class, studentId);   
        if(student != null) {
            student.getNotifications().size();
            return student;
        } else {
            throw new StudentNotFoundException("Student with Id: " + studentId + " cannot be found!");
        }
    }
    
    @Override
    public Long createNewStudent(Student newStudent) {
        em.persist(newStudent);
        em.flush(); 
        return newStudent.getStudentId();
    }
    
    @Override
    public void deleteStudent(Long studentId) throws StudentNotFoundException {
        Student studentToBeDeleted = retrieveStudentByStudentId(studentId);
        
        //for one to many
        for(Attendance attendance: studentToBeDeleted.getAttendances()) {
            em.remove(attendance);
        }       
        studentToBeDeleted.getAttendances().clear();
        
        //for many to many
        for(Notification notification: studentToBeDeleted.getNotifications()) {
            
            notification.getStudents().remove(studentToBeDeleted);            
            if (notification.getStudents().size() == 0) {                  
                em.remove(notification);
            }
        }       
        studentToBeDeleted.getNotifications().clear();
        
        for(Comment comment : studentToBeDeleted.getComments()) {           
            em.remove(comment);  
        }
        studentToBeDeleted.getComments().clear();
        
        for(Post post : studentToBeDeleted.getPosts()) {           
            em.remove(post);  
        }
        studentToBeDeleted.getPosts().clear();
        
        for(Event event : studentToBeDeleted.getEventsOrganised()) {           
            em.remove(event);  
        }
        studentToBeDeleted.getEventsOrganised().clear();
        
        for(Event event: studentToBeDeleted.getEvents()) {
            
            event.getStudents().remove(studentToBeDeleted);            
            if (event.getStudents().size() == 0) {                  
                em.remove(event);
            }
        }       
        
        em.remove(studentToBeDeleted); 
    }
    
    @Override
    public void updateStudent(Student tempStudent) throws StudentNotFoundException {
        
        Student studentToUpdate = em.find(Student.class, tempStudent.getStudentId());

        if (tempStudent.getName() != null)
            studentToUpdate.setName(tempStudent.getName());
        if (tempStudent.getEmail() != null)
            studentToUpdate.setEmail(tempStudent.getEmail());
        if (tempStudent.getEmail() != null)
            studentToUpdate.setPassword(tempStudent.getPassword());
        if (tempStudent.getUserName()!= null)
            studentToUpdate.setUserName(tempStudent.getUserName());
        if (tempStudent.getProfilePicture()!= null)
            studentToUpdate.setProfilePicture(tempStudent.getProfilePicture());
        if (tempStudent.getAccessRightEnum()!= null)
            studentToUpdate.setAccessRightEnum(tempStudent.getAccessRightEnum());
    }
}
