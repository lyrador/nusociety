/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author raihan
 */
@Entity
public class Society implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long societyId;
    
    @Column(nullable = false, length = 64)
    private String name;
    @Column(length = 128)
    private String description;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreated;
    
    @OneToMany(mappedBy = "society", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Announcement> announcements;
    
    @ManyToMany
    private List<SocietyCategory> societyCategories;
    @OneToMany(mappedBy = "society", cascade = {}, fetch = FetchType.LAZY)
    private List<Post> posts;
    @ManyToMany(mappedBy = "societies")
    private List<Staff> staffs;   
//    
//    @ManyToMany
//    private List<Student> students;
//    
//    @OneToMany(mappedBy = "society")
//    private List<Event> events;

    public Society() {
        this.announcements = new ArrayList<>();
        this.societyCategories = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.staffs = new ArrayList<>();
    }

    public Society(String name, String description, Date dateCreated) {
        this();
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public List<SocietyCategory> getSocietyCategories() {
        return societyCategories;
    }

    public void setSocietyCategories(List<SocietyCategory> societyCategories) {
        this.societyCategories = societyCategories;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (societyId != null ? societyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the societyCategoryId fields are not set
        if (!(object instanceof SocietyCategory)) {
            return false;
        }
        Society other = (Society) object;
        if ((this.societyId == null && other.societyId != null) || (this.societyId != null && !this.societyId.equals(other.societyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Society[ id=" + societyId + " ]";
    }
    
}
