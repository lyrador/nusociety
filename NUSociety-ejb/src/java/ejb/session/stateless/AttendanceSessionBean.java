/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AttendanceNotFoundException;

/**
 *
 * @author yeeda
 */
@Stateless
public class AttendanceSessionBean implements AttendanceSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Attendance> retrieveAllAttendances() {
        Query query = em.createQuery("SELECT a FROM Attendance a");     
        return query.getResultList();
    }
    
    @Override
    public Attendance retrieveAttendanceByAttendanceId(Long attendanceId) throws AttendanceNotFoundException{
        Attendance attendance = em.find(Attendance.class, attendanceId);
        
        if (attendance != null) {
            return attendance;
        } else {
            throw new AttendanceNotFoundException("Attendance with Id: " + attendanceId + " cannot be found!");
        }
    }
    
    @Override
    public Long createNewAttendance(Attendance newAttendance) {
        em.persist(newAttendance);
        em.flush(); 
        return newAttendance.getAttendanceId();
    }
    
    @Override
    public void deleteAttendance(Long attendanceId) throws AttendanceNotFoundException {
        Attendance attendanceToBeDeleted = retrieveAttendanceByAttendanceId(attendanceId);
        
        Student student = attendanceToBeDeleted.getStudent();
        student.getAttendances().remove(attendanceToBeDeleted);
        
        em.remove(attendanceToBeDeleted); 
    }
    
    @Override
    public void updateAttendance(Attendance tempAttendance) throws AttendanceNotFoundException {
        
        Attendance attendanceToUpdate = em.find(Attendance.class, tempAttendance.getAttendanceId());

        if (tempAttendance.getAttendedCount()!= null)
            attendanceToUpdate.setAttendedCount(tempAttendance.getAttendedCount());
        if (tempAttendance.getTotalCount()!= null)
            attendanceToUpdate.setTotalCount(tempAttendance.getTotalCount());
    }
}
