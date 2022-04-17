/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Comment;
import entity.Event;
import entity.Post;
import entity.Society;
import entity.Student;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author yeeda
 */
@Stateless
public class StudentSessionBean implements StudentSessionBeanLocal {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

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
    public List<Student> retrieveAllStudentsFromSocietyId(Long societyId) {
        Query query = em.createQuery("SELECT s FROM Student s, IN (s.memberSocieties) m WHERE m.societyId = :societyId"); 
        query.setParameter("societyId", societyId);
        return query.getResultList();
    }
    
    @Override
    public Student retrieveStudentByStudentId(Long studentId) throws StudentNotFoundException{
        Student student = em.find(Student.class, studentId);   
        if(student != null) {
            student.getPosts().size();
            student.getComments().size();
            student.getEvents().size();
            student.getEventsOrganised().size();
            student.getAttendances().size();
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
    public Long createNewStudentWithListOfSocietyIdsToBeLeaderOf(Student newStudent, List<Long> listOfSocietyIdsToBeLeaderOf) throws StudentNotFoundException, SocietyNotFoundException {
        em.persist(newStudent);
        em.flush(); 
        for (Long societyIdToBeLeaderOf : listOfSocietyIdsToBeLeaderOf) {
            setStudentLeaderOfSociety(newStudent.getStudentId(), societyIdToBeLeaderOf);
        }
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
        
        for(Society memberSociety: studentToBeDeleted.getMemberSocieties()) {
            memberSociety.getMemberStudents().remove(studentToBeDeleted);
        }
        
        for(Society leaderSociety: studentToBeDeleted.getLeaderSocieties()) {
            leaderSociety.getLeaderStudents().remove(studentToBeDeleted);
        }
        
        for(Society followedSociety: studentToBeDeleted.getFollowedSocieties()) {
            followedSociety.getFollowedStudents().remove(studentToBeDeleted);
        }
        
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
    public Student retrieveStudentByUsername(String username) throws StudentNotFoundException {
        
        Query query = em.createQuery("SELECT s FROM Student s WHERE s.userName = :inUsername");
        query.setParameter("inUsername", username);
        
        Student student = (Student) query.getSingleResult();
        if(student != null) {
            student.getPosts().size();
            student.getComments().size();
            student.getMemberSocieties().size();
            student.getFollowedSocieties().size();
            student.getEvents().size();
            student.getEventsOrganised().size();
            student.getAttendances().size();
            return student;
        } else {
            throw new StudentNotFoundException("Student with username: " + username + " cannot be found!");
        }
    }
    
    @Override
    public Student retrieveStudentByEmail(String email) throws StudentNotFoundException {
        
        Query query = em.createQuery("SELECT s FROM Student s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);
        
        Student student = (Student) query.getSingleResult();
        if(student != null) {
            student.getPosts().size();
            student.getComments().size();
            student.getEvents().size();
            student.getEventsOrganised().size();
            student.getAttendances().size();
            return student;
        } else {
            throw new StudentNotFoundException("Student with email: " + email + " cannot be found!");
        }
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
    }
    
    @Override
    public Student studentLogin(String username, String password) throws InvalidLoginCredentialException, StudentNotFoundException {
        
        try {
            Student student = retrieveStudentByUsername(username);
            
            if (student.getPassword().equals(password)) {
                student.getEvents().size();
                student.getEventsOrganised().size();
                student.getAttendances().size();
                student.getComments().size();
                student.getPosts().size();
                //added this
                student.getMemberSocieties().size();
                student.getFollowedSocieties().size();
                student.getLeaderSocieties().size();
                return student;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (StudentNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public Student studentFollow(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student student = retrieveStudentByStudentId(studentId);
        Society societyToFollow = societySessionBeanLocal.retrieveSocietyById(societyId);

        student.getFollowedSocieties().add(societyToFollow);
        societyToFollow.getFollowedStudents().add(student);

        System.out.println(studentId + " followed " + societyId + "!");
        return student;
    }
    
    @Override
    public Student studentUnfollow(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student student = retrieveStudentByStudentId(studentId);
        Society societyToFollow = societySessionBeanLocal.retrieveSocietyById(societyId);

        student.getFollowedSocieties().remove(societyToFollow);
        societyToFollow.getFollowedStudents().remove(student);

        System.out.println(studentId + " unfollowed " + societyId + "!");
        return student;
    }
    
    @Override
    public void setStudentLeaderOfSociety(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student studentLeader = retrieveStudentByStudentId(studentId);
        Society leaderSociety = societySessionBeanLocal.retrieveSocietyById(societyId);
        
        studentLeader.getLeaderSocieties().size();
        studentLeader.getMemberSocieties().size();

        studentLeader.getLeaderSocieties().add(leaderSociety);
        leaderSociety.getLeaderStudents().add(studentLeader);
        
        if (!studentLeader.getMemberSocieties().contains(leaderSociety)) {
            studentLeader.getMemberSocieties().add(leaderSociety);   
        }   
        
        if (!leaderSociety.getMemberStudents().contains(studentLeader)) {
            leaderSociety.getMemberStudents().add(studentLeader);
        }   
//        
//        studentLeader.getLeaderSocieties().size();
//        studentLeader.getMemberSocieties().size();

        System.out.println(studentId + " is now leader of " + societyId + "!");
    }
    
    @Override
    public List<Society> retrieveSocietiesLedByStudent(Long studentId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student studentLeader = retrieveStudentByStudentId(studentId);
        studentLeader.getLeaderSocieties().size();
        studentLeader.getMemberSocieties().size();
        return studentLeader.getLeaderSocieties();
    }
    
    @Override
    public List<Society> retrieveSocietiesForMemberPositions(Long studentId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student studentLeader = retrieveStudentByStudentId(studentId);
        studentLeader.getMemberSocieties().size();
        List<Society> societies= studentLeader.getMemberSocieties();
        
        for (Society society: studentLeader.getLeaderSocieties()) {
            if (societies.contains(society)) {
                societies.remove(society);
            }
        }
        return societies;
    }
    
    @Override
    public List<Society> retrieveSocietiesWhereStudentIsNotIn(Long studentId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student student= retrieveStudentByStudentId(studentId);   
        student.getLeaderSocieties().size();
        student.getMemberSocieties().size();
        
        List<Society> availableSocieties = new ArrayList<Society>();
        List<Society> memberSocieties = societySessionBeanLocal.retrieveSocietiesForMember(student.getStudentId());
        memberSocieties.size();
        List<Society> allSocieties = societySessionBeanLocal.retrieveAllSocieties();
        
        
        for (Society society: allSocieties) {
            System.out.println("MEMBER OF " + society.getName());
            if (!memberSocieties.contains(society)) {
                availableSocieties.add(society);
            }
        }
        student.getLeaderSocieties().size();
        student.getMemberSocieties().size();
        
        return availableSocieties;
    }
    
    @Override
    public void unlinkStudentLeaderFromSociety(Long studentId, Long societyId) throws StudentNotFoundException, SocietyNotFoundException {
        
        Student studentLeader = retrieveStudentByStudentId(studentId);
        Society leaderSociety = societySessionBeanLocal.retrieveSocietyById(societyId);
        
        studentLeader.getLeaderSocieties().size();
        leaderSociety.getMemberStudents().size();

        studentLeader.getLeaderSocieties().remove(leaderSociety);      
        leaderSociety.getLeaderStudents().remove(studentLeader);
        
        if (!studentLeader.getMemberSocieties().contains(leaderSociety)) {
            studentLeader.getMemberSocieties().add(leaderSociety);   
        }   
        
        if (!leaderSociety.getMemberStudents().contains(studentLeader)) {
            leaderSociety.getMemberStudents().add(studentLeader);
        }   

        studentLeader.getLeaderSocieties().size();
        leaderSociety.getMemberStudents().size();

        System.out.println(studentId + " is no longer leader of " + societyId + "!");
    }
}
