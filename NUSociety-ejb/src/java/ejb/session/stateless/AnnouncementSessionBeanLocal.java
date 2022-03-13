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

/**
 *
 * @author raihan
 */
@Local
public interface AnnouncementSessionBeanLocal {

    public Announcement createNewAnnouncement(Announcement announcement);

    public Announcement retrieveAnnouncementById(Long id) throws AnnouncementNotFoundException;

    public List<Announcement> retrieveAllAnnouncements();

    public void updateAnnouncement(Announcement announcement) throws AnnouncementNotFoundException;

    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException;
    
}
