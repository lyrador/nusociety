/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Society;
import entity.SocietyCategory;
import entity.Staff;
import entity.Student;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
@Stateless
public class SocietySessionBean implements SocietySessionBeanLocal {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;
    
    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Society createNewSociety(Society society, List<Long> categoryIds, List<Long> staffIds) throws CreateSocietyException, UnknownPersistenceException {

        System.out.println("cat id size " + categoryIds.size());
        System.out.println("staff id size " + staffIds.size());
        try {
            if (categoryIds.isEmpty()) {
                throw new CreateSocietyException("The new society must be associated with at least one society category");
            } else if (staffIds.isEmpty()) {
                throw new CreateSocietyException("The new society must be associated with at least one staff");
            } else {

                for (Long id : categoryIds) {
                    SocietyCategory category = societyCategorySessionBeanLocal.retrieveSocietyCategoryById(id);
                    society.getSocietyCategories().add(category);
                    category.getSocieties().add(society);
                }

                for (Long id : staffIds) {
                    Staff staff = staffSessionBeanLocal.retrieveStaffById(id);
                    society.getStaffs().add(staff);
                    staff.getSocieties().add(society);
                }

                em.persist(society);
                em.flush();

                return society;
            }

        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        } catch (SocietyCategoryNotFoundException | StaffNotFoundException ex) {
            throw new CreateSocietyException("An error has occurred while creating the new society: " + ex.getMessage());
        }

    }

    @Override
    public Society retrieveSocietyById(Long societyId) throws SocietyNotFoundException {
        Society society = em.find(Society.class, societyId);

        try {
            society.getAnnouncements().size();
            society.getSocietyCategories().size();
            society.getStaffs().size();
            society.getPosts().size();
            society.getMemberStudents().size();
            society.getFollowedStudents().size();
            society.getEvents().size();
            society.getLeaderStudents().size();
            society.getAttendances().size();
            return society;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new SocietyNotFoundException("Society Id " + societyId + " does not exist!");
        }
    }

    @Override
    public List<Society> retrieveSocietiesForMember(Long memberId) {
        Query query = em.createQuery("SELECT s FROM Society s, IN (s.memberStudents) m WHERE m.studentId = :inStudentId");
        query.setParameter("inStudentId", memberId);
        List<Society> societies = query.getResultList();

        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
            society.getSurveys().size();
            society.getAnnouncements().size();
            society.getFollowedStudents().size();
            society.getMemberStudents().size();
            society.getPosts().size();
            society.getEvents().size();
            society.getAttendances().size();
            society.getLeaderStudents().size();
        }
        if (societies.isEmpty()) {
        System.out.println("**** The society is empty"); 
        System.out.println("**** The student id is " + memberId); 
        }
        return query.getResultList();
    }
    
    @Override
    public List<Society> retrieveSocietiesForStaff(Long staffId) {
        Query query = em.createQuery("SELECT s FROM Society s, IN (s.staffs) m WHERE m.staffId = :inStaffId");
        query.setParameter("inStaffId", staffId);
        List<Society> societies = query.getResultList();

        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
        }
        if (societies.isEmpty()) {
        System.out.println("**** The society is empty"); 
        System.out.println("**** The staff id is " + staffId); 
        }
        return query.getResultList();
    }

    public void removeStudentFromSociety(Long societyId, Long studentId) throws SocietyNotFoundException, StudentNotFoundException {
        Society society = em.find(Society.class, societyId);
        Student student = em.find(Student.class, studentId);
        try {
            Query query = em.createQuery("SELECT a FROM Attendance a WHERE a.student.studentId = :studentId AND a.society.societyId = :societyId");
            query.setParameter("studentId", studentId);
            query.setParameter("societyId", societyId);
            Attendance attendanceToRemove = (Attendance) query.getSingleResult();
            student.getAttendances().remove(attendanceToRemove);  
            System.out.println("REMOVING ATTENDANCE: " + attendanceToRemove.getAttendanceId());
            em.remove(attendanceToRemove);
            society.getMemberStudents().size();
            society.getMemberStudents().remove(student);
            student.getMemberSocieties().remove(society);
            society.getLeaderStudents().size();
            society.getLeaderStudents().remove(student);
            student.getLeaderSocieties().remove(society);
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new SocietyNotFoundException("Society Id " + societyId + " does not exist!");
        }
    }

    public void addStudentToSociety(Long societyId, Long studentId) throws SocietyNotFoundException, StudentNotFoundException {
        Society society = em.find(Society.class, societyId);
        Student student = em.find(Student.class, studentId);
        try {
            society.getMemberStudents().size();
            student.getMemberSocieties().size();
            society.getMemberStudents().add(student);
            student.getMemberSocieties().add(society);
            
            Attendance tempAttendance = new Attendance(1, 1);
            tempAttendance.setStudent(student);
            student.getAttendances().add(tempAttendance);
            tempAttendance.setSociety(society);
            society.getAttendances().add(tempAttendance);
            attendanceSessionBeanLocal.createNewAttendance(tempAttendance);
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new SocietyNotFoundException("Society Id " + societyId + " does not exist!");
        }
    }

    @Override
    public List<Society> retrieveAllSocieties() {
        Query query = em.createQuery("SELECT s FROM Society s");
        List<Society> societies = query.getResultList();

        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
            society.getSurveys().size();
            society.getAnnouncements().size();
            society.getFollowedStudents().size();
            society.getMemberStudents().size();
            society.getPosts().size();
            society.getEvents().size();
            society.getAttendances().size();
            society.getLeaderStudents().size();
        }
        return query.getResultList();
    }

    @Override
    public void updateSociety(Society newSociety, List<Long> categoryIds, List<Long> staffIds) throws SocietyNotFoundException, SocietyCategoryNotFoundException, StaffNotFoundException {

        Society societyToUpdate = retrieveSocietyById(newSociety.getSocietyId());

        if (societyToUpdate.getSocietyId().equals(newSociety.getSocietyId())) {

            //Update categories
            for (SocietyCategory category : newSociety.getSocietyCategories()) {
                category.getSocieties().remove(societyToUpdate);
            }
            societyToUpdate.getSocietyCategories().clear();

            for (Long id : categoryIds) {
                SocietyCategory category = societyCategorySessionBeanLocal.retrieveSocietyCategoryById(id);
                societyToUpdate.getSocietyCategories().add(category);
            }

            //Update staffs
            for (Staff staff : newSociety.getStaffs()) {
                staff.getSocieties().remove(societyToUpdate);
            }
            societyToUpdate.getStaffs().clear();

            for (Long id : staffIds) {
                Staff staff = staffSessionBeanLocal.retrieveStaffById(id);
                societyToUpdate.getStaffs().add(staff);
            }

            //Update everything else
            societyToUpdate.setName(newSociety.getName());
            societyToUpdate.setDescription(newSociety.getDescription());
            societyToUpdate.setProfilePicture(newSociety.getProfilePicture());
        }

    }

    @Override
    public void deleteSociety(Long societyId) throws SocietyNotFoundException {
        Society societyToDelete = retrieveSocietyById(societyId);

        //Announcement
        if (!societyToDelete.getAnnouncements().isEmpty()) {
            societyToDelete.getAnnouncements().clear();
        }
        
        //Feedback Survey
        if (!societyToDelete.getSurveys().isEmpty()) {
            societyToDelete.getSurveys().clear();
        }

        //SocietyCategory
        for (SocietyCategory category : societyToDelete.getSocietyCategories()) {
            category.getSocieties().remove(societyToDelete);
        }

        //Staff
        for (Staff staff : societyToDelete.getStaffs()) {
            staff.getSocieties().remove(societyToDelete);
        }

        //Post
        if (!societyToDelete.getPosts().isEmpty()) {
            societyToDelete.getPosts().clear();
        }

        //Followed Students
        for (Student student : societyToDelete.getFollowedStudents()) {
            student.getMemberSocieties().remove(societyToDelete);
        }
        societyToDelete.getMemberStudents().clear();

        //Member Students
        for (Student student : societyToDelete.getMemberStudents()) {
            student.getFollowedSocieties().remove(societyToDelete);
        }
        societyToDelete.getMemberStudents().clear();

        //Event
        if (!societyToDelete.getEvents().isEmpty()) {
            societyToDelete.getEvents().clear();
        }
        em.remove(societyToDelete);
    }

    @Override
    public List<Society> searchSocietyByName(String searchString) {
        Query query = em.createQuery("SELECT s FROM Society s WHERE s.name LIKE :inSearchString ORDER BY s.societyId ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<Society> societies = query.getResultList();

        for (Society society : societies) {
            society.getSocietyCategories().size();
            society.getStaffs().size();
        }
        return query.getResultList();
    }

}
