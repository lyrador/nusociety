/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Announcement;
import java.util.List;
import javax.ejb.Local;
import util.exception.AnnouncementNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface AnnouncementSessionBeanLocal {

    public Announcement createNewAnnouncement(Announcement announcement, Long societyId) throws SocietyNotFoundException;

    public Announcement retrieveAnnouncementById(Long id) throws AnnouncementNotFoundException;

    public List<Announcement> retrieveAllAnnouncements();

    public void updateAnnouncement(Announcement announcement) throws AnnouncementNotFoundException;

    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException;

    public List<Announcement> retrieveAnnouncementsForSociety(Long id);

    public List<Announcement> retrieveAllMyAnnouncements(Long studentId) throws StudentNotFoundException ;
    
}
