/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.AttendanceSessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Society;
import entity.SocietyCategory;
import entity.Staff;
import entity.Student;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.CreateSocietyCategoryException;
import util.exception.CreateSocietyException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Singleton
@LocalBean
@Startup
public class DataIntSessionBean {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        List<Long> staffIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        if (em.find(Student.class, 1l) == null) {
            studentSessionBeanLocal.createNewStudent(new Student("Alex", "alex@gmail.com", "password", "alex", "www.profilepic/alex", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Betty", "betty@gmail.com", "password", "betty", "www.profilepic/betty", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Carl", "carl@gmail.com", "password", "carl", "www.profilepic/carl", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("David", "david@gmail.com", "password", "david", "www.profilepic/david", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Elaine", "elaine@gmail.com", "password", "elaine", "www.profilepic/elaine", AccessRightEnum.MEMBER));
        }

        List<Student> students = studentSessionBeanLocal.retrieveAllStudents();
        for (Student student : students) {
            System.out.println(student.getName());
            System.out.println(student.getEmail());
        }

        if (em.find(SocietyCategory.class, 1l) == null) {

            try {
                SocietyCategory category1 = societyCategorySessionBeanLocal.createNewSocietyCategory(new SocietyCategory("Sports"));
                SocietyCategory category2 = societyCategorySessionBeanLocal.createNewSocietyCategory(new SocietyCategory("Clubs"));
                SocietyCategory category3 = societyCategorySessionBeanLocal.createNewSocietyCategory(new SocietyCategory("Performing Arts"));
                categoryIds.add(category1.getSocietyCategoryId());
                categoryIds.add(category2.getSocietyCategoryId());
            } catch (CreateSocietyCategoryException | SocietyCategoryNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (em.find(Staff.class, 1l) == null) {
            Long staffId = staffSessionBeanLocal.createNewStaff(new Staff("staff@gmail.com", "password", "staff", "www.staff.com/profilepic"));
            staffIds.add(staffId);
        }

        if (em.find(Society.class, 1l) == null) {

            try {
                societySessionBeanLocal.createNewSociety(new Society("Floorball", "Sticks and Balls", new Date()), categoryIds, staffIds);
            } catch (CreateSocietyException | UnknownPersistenceException e) {
                e.printStackTrace();
            }
        }

        if (em.find(Attendance.class, 1l) == null) {
            //add all students to first society for testing
            try {
                Society floorball = societySessionBeanLocal.retrieveSocietyById((long) 1);
                List<Student> studentsToAdd = studentSessionBeanLocal.retrieveAllStudents();
                for (Student student : studentsToAdd) {
                    floorball.getStudents().add(student);
                    student.getMemberSocieties().add(floorball);

                    Attendance tempAttendance = new Attendance(1, 1);
                    tempAttendance.setStudent(student);
                    student.getAttendances().add(tempAttendance);
                    attendanceSessionBeanLocal.createNewAttendance(tempAttendance);
                }
            } catch (SocietyNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

}
