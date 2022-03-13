/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Announcement;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AnnouncementNotFoundException;

/**
 *
 * @author raihan
 */
@Stateless
public class AnnouncementSessionBean implements AnnouncementSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Announcement createNewAnnouncement(Announcement announcement) {
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
    public void updateAnnouncement(Announcement announcement) throws AnnouncementNotFoundException {
        
        if (announcement.getAnnouncementId() != null) {
            Announcement annoucementToUpdate = retrieveAnnouncementById(announcement.getAnnouncementId());
            
            annoucementToUpdate.setAnnouncementContent(announcement.getAnnouncementContent());
        } else {
            throw new AnnouncementNotFoundException("Announcement " + announcement.getAnnouncementId() + " does not exist!");
        }
    }
    
    @Override
    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException{
        
        Announcement announcementToDelete = retrieveAnnouncementById(announcementId);
        
        em.remove(announcementToDelete);
    }
    
}
