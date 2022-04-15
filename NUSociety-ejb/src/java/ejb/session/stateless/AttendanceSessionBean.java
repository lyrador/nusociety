/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AttendanceNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author yeeda
 */
@Stateless
public class AttendanceSessionBean implements AttendanceSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;
    
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Attendance> retrieveAllAttendances() {
        Query query = em.createQuery("SELECT a FROM Attendance a");
        return query.getResultList();
    }

    @Override
    public Attendance retrieveAttendanceByAttendanceId(Long attendanceId) throws AttendanceNotFoundException {
        Attendance attendance = em.find(Attendance.class, attendanceId);

        if (attendance != null) {
            return attendance;
        } else {
            throw new AttendanceNotFoundException("Attendance with Id: " + attendanceId + " cannot be found!");
        }
    }

    @Override
    public Attendance retrieveAttendanceFromStudentIdAndSocietyId(Long studentId, Long societyId) {
//        Query query = em.createQuery("SELECT a FROM Attendance a WHERE a.student.studentId = :studentId AND a.society.societyId = :societyId");
//        query.setParameter("studentId", studentId);
//        List<Attendance> allAttendancesBelongingToStudent = query.getResultList(); 
//        System.out.println(studentId + " | " + allAttendancesBelongingToStudent.size() + " = allAttendancesBelongingToStudent");
//        
//        Query query2 = em.createQuery("SELECT a FROM Attendance a, IN (a.student.memberSocieties) m WHERE m.societyId = :societyId");
//        query2.setParameter("societyId", societyId);
//        List<Attendance> allAttendancesBelongingToSociety = query2.getResultList();
//        System.out.println(studentId + " | " + allAttendancesBelongingToSociety.size() + " = allAttendancesBelongingToSociety");
//
//        for (Attendance attendance1 : allAttendancesBelongingToStudent) {
//            for (Attendance attendance2 : allAttendancesBelongingToSociety) {
//                if (attendance1.getAttendanceId() == attendance2.getAttendanceId()) {
//                    System.out.println(attendance2.getAttendanceId());
//                    return attendance2;
//                }
//            }
//        }
//        return null;
//        
        Query query = em.createQuery("SELECT a FROM Attendance a WHERE a.student.studentId = :studentId AND a.society.societyId = :societyId");
        query.setParameter("studentId", studentId);
        query.setParameter("societyId", societyId);
        return (Attendance) query.getSingleResult();
    }

    @Override
    public HashMap<Long, Attendance> retrieveMapAttendancesFromStudentListAndSocietyId(List<Student> students, Long societyId) {
        HashMap<Long, Attendance> hashMap = new HashMap<Long, Attendance>();
        for (Student student : students) {
            Attendance tempAttendance = retrieveAttendanceFromStudentIdAndSocietyId(student.getStudentId(), societyId);
            hashMap.put(student.getStudentId(), tempAttendance);
        }
        return hashMap;
    }

    @Override
    public Long createNewAttendance(Attendance newAttendance) throws SocietyNotFoundException, StudentNotFoundException {
        
        if (!newAttendance.getSociety().getAttendances().contains(newAttendance)) {
            Long societyId = newAttendance.getSociety().getSocietyId();
            societySessionBeanLocal.retrieveSocietyById(societyId).getAttendances().add(newAttendance);     
        }

        if (!newAttendance.getStudent().getAttendances().contains(newAttendance)) {
            Long studentId = newAttendance.getStudent().getStudentId();
            studentSessionBeanLocal.retrieveStudentByStudentId(studentId).getAttendances().add(newAttendance);
        }
                
        em.persist(newAttendance);
        em.flush();
        return newAttendance.getAttendanceId();
    }

    @Override
    public void deleteAttendance(Long attendanceId) throws AttendanceNotFoundException {
        Attendance attendanceToBeDeleted = retrieveAttendanceByAttendanceId(attendanceId);

        Student student = attendanceToBeDeleted.getStudent();
        student.getAttendances().remove(attendanceToBeDeleted);
        attendanceToBeDeleted.getSociety().getAttendances().remove(attendanceToBeDeleted);

        em.remove(attendanceToBeDeleted);
    }

    @Override
    public void updateAttendance(Attendance tempAttendance) throws AttendanceNotFoundException {

        Attendance attendanceToUpdate = em.find(Attendance.class, tempAttendance.getAttendanceId());

        if (tempAttendance.getAttendedCount() != null) {
            attendanceToUpdate.setAttendedCount(tempAttendance.getAttendedCount());
        }
        if (tempAttendance.getTotalCount() != null) {
            attendanceToUpdate.setTotalCount(tempAttendance.getTotalCount());
        }
    }
}
