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
import ejb.session.stateless.FeedbackSurveySessionBeanLocal;
import ejb.session.stateless.PostSessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Comment;
import entity.EventCategory;
import entity.FeedbackSurvey;
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
    private FeedbackSurveySessionBeanLocal feedbackSurveySessionBeanLocal;

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
        List<Long> categoryIds4 = new ArrayList<>();

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
                SocietyCategory category4 = societyCategorySessionBeanLocal.createNewSocietyCategory(new SocietyCategory("Halls"));
                SocietyCategory category5 = societyCategorySessionBeanLocal.createNewSocietyCategory(new SocietyCategory("Residences"));
                categoryIds1.add(category1.getSocietyCategoryId());
                categoryIds2.add(category5.getSocietyCategoryId());
                categoryIds2.add(category2.getSocietyCategoryId());
                categoryIds3.add(category3.getSocietyCategoryId());
                categoryIds4.add(category4.getSocietyCategoryId());
                categoryIds4.add(category1.getSocietyCategoryId());
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
                societySessionBeanLocal.createNewSociety(new Society("NUS Floorball", 
                        "Floorball is a type of floor hockey with five players and a goalkeeper in each team. Men and women play indoors with "
                                + "96–115.5 cm-long (37.8–45.5 in) sticks and a 70–72 mm-diameter (2.76–2.83 in) plastic ball with holes. Matches "
                                + "are played in three twenty-minute periods. The sport of bandy also played a role in the game's development.", 
                                new Date(), "floorball_society.jpeg"), categoryIds1, staffIds);
                
                societySessionBeanLocal.createNewSociety(new Society("NUS Choir", "A choir is a musical ensemble of singers. Choral music, in turn, "
                        + "is the music written specifically for such an ensemble to perform. Choirs may perform music from the classical music repertoire, "
                        + "which spans from the medieval era to the present, or popular music repertoire. Most choirs are led by a conductor, who leads the "
                        + "performances with arm, hand, and facial gestures.", new Date(), "choir_society.jpeg"), categoryIds3, staffIds);
                
                societySessionBeanLocal.createNewSociety(new Society("NUS Concert Band", "A concert band, variously also called a wind ensemble, symphonic "
                        + "band, wind symphony, wind orchestra, wind band, symphonic winds, symphony band, or symphonic wind ensemble, is a performing ensemble "
                        + "consisting of members of the woodwind, brass, and percussion families of instruments, and occasionally including the harp and the "
                        + "double bass or bass guitar. On rare occasions, additional non-traditional instruments may be added to such ensembles such as piano, "
                        + "synthesizer, or electric guitar.", new Date(), "band_society.jpeg"), categoryIds3, staffIds);
                
                societySessionBeanLocal.createNewSociety(new Society("Tembusu College Art Club", "The Art Club is a place for practicing artists to hone in "
                        + "on their skills, develop their techniques and portfolios, collaborate with other artists like themselves, create bonds with the "
                        + "community through the arts, and learn how to work together through group projects that will beautify the school and community.", 
                        new Date(), "artclub_society.jpeg"), categoryIds2, staffIds);
                
                societySessionBeanLocal.createNewSociety(new Society("Sheares Hall Swimming", "Swimming is an individual or team racing sport that requires the use of one's entire "
                        + "body to move through water. The sport takes place in pools or open water (e.g., in a sea or lake). Competitive swimming is one "
                        + "of the most popular Olympic sports,[1] with varied distance events in butterfly, backstroke, breaststroke, freestyle, and "
                        + "individual medley. In addition to these individual events, four swimmers can take part in either a freestyle or medley relay. "
                        + "A medley relay consists of four swimmers who will each swim a different stroke, ordered as backstroke, breaststroke, butterfly "
                        + "and freestyle.", new Date(), "swimming_society.jpeg"), categoryIds4, staffIds);
//                
//                

            } catch (CreateSocietyException | UnknownPersistenceException e) {
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
        
        if (em.find(FeedbackSurvey.class, 1l) == null) {
            try {
                FeedbackSurvey survey1 = new FeedbackSurvey("IHG Event", "Well organized. Good job!", new Date(), 5);
                FeedbackSurvey survey2 = new FeedbackSurvey("Training sessions", "Could be more intense", new Date(), 3);
                
                feedbackSurveySessionBeanLocal.submitSurvey(survey1, 1l);
                feedbackSurveySessionBeanLocal.submitSurvey(survey2, 1l);
            } catch(SocietyNotFoundException ex) {
                ex.getMessage();
            }
        }
        
        
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