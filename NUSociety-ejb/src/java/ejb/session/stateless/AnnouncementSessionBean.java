/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Announcement;
import entity.Society;
import entity.Student;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AnnouncementNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author raihan
 */
@Stateless
public class AnnouncementSessionBean implements AnnouncementSessionBeanLocal {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Announcement createNewAnnouncement(Announcement announcement, Long societyId) throws SocietyNotFoundException {
        
        Society society = societySessionBeanLocal.retrieveSocietyById(societyId);
        announcement.setSociety(society);
        society.getAnnouncements().add(announcement);
        
        em.persist(announcement);
        em.flush();
        
        return announcement;
    }
    
    @Override
    public Announcement retrieveAnnouncementById(Long id) throws AnnouncementNotFoundException {
        Announcement announcement = em.find(Announcement.class, id);
        
        if (announcement != null) {
            return announcement;
        } else {
            throw new AnnouncementNotFoundException("Announcement " + id + " does not exist!");
        }
    }
    
    @Override
    public List<Announcement> retrieveAllAnnouncements() {
        Query query = em.createQuery("SELECT a FROM Announcement a");
        List<Announcement> announcements = query.getResultList();
        
        for (Announcement announcement : announcements) {
            announcement.getSociety();
        }
        
        return announcements;
    }
    
    @Override
    public List<Announcement> retrieveAllMyAnnouncements(Long studentId) throws StudentNotFoundException {  
        List<Announcement> allMyAnnouncements = new ArrayList<Announcement>();
        Student student = studentSessionBeanLocal.retrieveStudentByStudentId(studentId);
        List<Society> memberSocieties = student.getMemberSocieties();
        List<Society> followingSocieties = student.getFollowedSocieties();

        for (Society society: memberSocieties) {
            for (Announcement announcement : society.getAnnouncements()) {
                System.out.println("HELLO");
                if (announcement.isOnlyForMembers() == true) {
                    allMyAnnouncements.add(announcement);
                }
            }
        }

        for (Society society: followingSocieties) {
            for (Announcement announcement : society.getAnnouncements()) {
                System.out.println("BYE");
                if (announcement.isOnlyForMembers() == false) {
                    allMyAnnouncements.add(announcement);
                }
            }
        }
        
        return allMyAnnouncements;
    }
    
//    @Override
//    public List<Announcement> retrieveAllAnnouncementsFromStudentId() {
//        Query query = em.createQuery("SELECT a FROM Announcement a WHERE a.");
//        List<Announcement> announcements = query.getResultList();
//        
//        for (Announcement announcement : announcements) {
//            announcement.getSociety();
//        }
//        
//        return announcements;
//    }
    
    @Override
    public List<Announcement> retrieveAnnouncementsForSociety(Long id) {
        Query query = em.createQuery("SELECT a FROM Announcement a WHERE a.society.societyId = :inSocietyId");
        query.setParameter("inSocietyId", id);
        
        List<Announcement> announcements = query.getResultList();
        
        for (Announcement announcement : announcements) {
            announcement.getSociety();
        }
        
        return announcements;
    }
    
    @Override
    public void updateAnnouncement(Announcement announcement) throws AnnouncementNotFoundException {
        
        if (announcement.getAnnouncementId() != null) {
            Announcement announcementToUpdate = retrieveAnnouncementById(announcement.getAnnouncementId());
            
            announcementToUpdate.setAnnouncementContent(announcement.getAnnouncementContent());
            announcementToUpdate.getSociety().getAnnouncements().remove(announcement);
            announcementToUpdate.getSociety().getAnnouncements().add(announcementToUpdate);
            
        } else {
            throw new AnnouncementNotFoundException("Announcement " + announcement.getAnnouncementId() + " does not exist!");
        }
    }
    
    @Override
    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException{
        
        Announcement announcementToDelete = retrieveAnnouncementById(announcementId);
        
        announcementToDelete.getSociety().getAnnouncements().remove(announcementToDelete);
        
        em.remove(announcementToDelete);
    }
    
}
