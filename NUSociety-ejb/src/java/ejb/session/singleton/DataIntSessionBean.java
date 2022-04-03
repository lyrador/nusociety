/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.AttendanceSessionBeanLocal;
import ejb.session.stateless.CommentSessionBeanLocal;
import ejb.session.stateless.EventCategorySessionBeanLocal;
import ejb.session.stateless.EventSessionBeanLocal;
import ejb.session.stateless.PostSessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Comment;
import entity.EventCategory;
import entity.Post;
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
import util.exception.EventCategoryAlreadyExistsException;
import util.exception.PostNotFoundException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;
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
    private EventCategorySessionBeanLocal eventCategorySessionBeanLocal;

    @EJB(name = "CommentSessionBeanLocal")
    private CommentSessionBeanLocal commentSessionBeanLocal;

    @EJB
    private PostSessionBeanLocal postSessionBeanLocal;

    @EJB
    private EventSessionBeanLocal eventSessionBeanLocal;

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;

    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal;

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        List<Long> staffIds = new ArrayList<>();
        List<Long> eventCategoryIds = new ArrayList<>(); 
        List<Long> categoryIds1 = new ArrayList<>();
        List<Long> categoryIds2 = new ArrayList<>();
        List<Long> categoryIds3 = new ArrayList<>();

        if (em.find(Student.class, 1l) == null) {
            studentSessionBeanLocal.createNewStudent(new Student("Alex", "alex@gmail.com", "password", "alex", "alexPic.png", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Betty", "betty@gmail.com", "password", "betty", "bettyPic.png", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Carl", "carl@gmail.com", "password", "carl", "carlPic.png", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("David", "david@gmail.com", "password", "david", "davidPic.png", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Elaine", "elaine@gmail.com", "password", "elaine", "elainePic.png", AccessRightEnum.LEADER));
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
                categoryIds1.add(category1.getSocietyCategoryId());
                categoryIds2.add(category2.getSocietyCategoryId());
                categoryIds2.add(category3.getSocietyCategoryId());
                categoryIds3.add(category3.getSocietyCategoryId());
            } catch (CreateSocietyCategoryException | SocietyCategoryNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (em.find(Staff.class, 1l) == null) {
            Long staffId = staffSessionBeanLocal.createNewStaff(new Staff("staff@gmail.com", "password", "staff", "www.staff.com/profilepic"));
            staffIds.add(staffId);
        }
        
        if(em.find(EventCategory.class, 1l) == null) {
            Long eventCategoryId1 = eventCategorySessionBeanLocal.createNewEventCategory(new EventCategory("Sports")); 
            Long eventCategoryId2 = eventCategorySessionBeanLocal.createNewEventCategory(new EventCategory("Interest Groups"));
            eventCategoryIds.add(eventCategoryId1); 
        }

        if (em.find(Society.class, 1l) == null) {

            try {
                societySessionBeanLocal.createNewSociety(new Society("Floorball", "Sticks and Balls", new Date(), "Harry-Maguire-2-1.jpg"), categoryIds1, staffIds);

                Student alex = studentSessionBeanLocal.retrieveStudentByUsername("alex");
                Society floorball = societySessionBeanLocal.retrieveSocietyById(1l);

//                List<Student> fbStuList = new ArrayList<>();
//                fbStuList.add(alex);
//                floorball.setMemberStudents(fbStuList);
//
//                List<Society> alexSocList = new ArrayList<>();
//                alexSocList.add(floorball);
//                alex.setMemberSocieties(alexSocList);
                societySessionBeanLocal.createNewSociety(new Society("Choir Club", "We love singing", new Date(), "220327-chris-rock-will-smith-3-ew-1052p-acc676.jpg"), categoryIds2, staffIds);
                societySessionBeanLocal.createNewSociety(new Society("NUS Band", "Banging the drums as hard as we can", new Date(), "1030_two-students-looking-at-tablet-1028x579.jpg"), categoryIds3, staffIds);
//                
//                if (em.find(Event.class, 1l) == null) {
//            
//                    try {
//                        Long eventId = eventSessionBeanLocal.createNewEvent(new Event("Floorball Finals", "NUS vs NTU", "USC", new Date(), new Date(), 20, alex, floorball));
//                        Event event = eventSessionBeanLocal.retrieveEventById(eventId);
//                        List<EventCategory> categories = new ArrayList<EventCategory>();
//                        categories.add(new EventCategory("balls"));
//                        event.setCategories(categories);
//                    } catch (EventAlreadyExistsException | EventNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }

            } catch (CreateSocietyException | UnknownPersistenceException | StudentNotFoundException | SocietyNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (em.find(Attendance.class, 1l) == null && em.find(Attendance.class, 2l) == null && em.find(Attendance.class, 3l) == null && em.find(Attendance.class, 4l) == null && em.find(Attendance.class, 5l) == null) {
            //add all students to first society for testing
            try {
                Society floorball = societySessionBeanLocal.retrieveSocietyById((long) 1);
                List<Student> studentsToAdd = studentSessionBeanLocal.retrieveAllStudents();
                for (Student student : studentsToAdd) {
                    student.getMemberSocieties().size();
                    student.getFollowedSocieties().size();
                    floorball.getFollowedStudents().size();
                    floorball.getMemberStudents().size();

                    floorball.getMemberStudents().add(student);
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

        students = studentSessionBeanLocal.retrieveAllStudents();

        String[] imageLink = {"alexPostPic.jpg", "bettyPostPic.jpg", "carlPostPic.jpg",
            "davidPostPic.jpg", "elainePostPic.jpg"};
        
        
        if (em.find(Post.class, 1l) == null) {
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);

                for (int j = 0; j < s.getMemberSocieties().size(); j++) {
                    System.out.println(s.getMemberSocieties().get(j));
                    try {
                        Long pId = postSessionBeanLocal.createNewPost(new Post("Hi I'm " + students.get(i).getName() +"!", imageLink[i],
                                s, s.getMemberSocieties().get(j)));
                        Post p = postSessionBeanLocal.retrievePostById(pId);

                        commentSessionBeanLocal.createComment(new Comment(new Date(), "My name is " + students.get(i).getName() + ", nice to meet you guys!", p, s));
                    } catch (PostNotFoundException ex) {
                        System.out.println("Error");
                    }
                }
            }

            System.out.println("POST INIT SUCCESS!");
        }
    }
}