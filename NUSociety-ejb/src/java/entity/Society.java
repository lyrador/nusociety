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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
    
    @Column(nullable = false, unique = true, length = 64)
    private String name;
    @Column(nullable = false, length = 1000)
    private String description;
    @Column(nullable = true, length = 256)
    private String profilePicture;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreated;
    
    @OneToMany(mappedBy = "society", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Announcement> announcements;
    
    @ManyToMany(cascade = {}, fetch = FetchType.LAZY)
    private List<SocietyCategory> societyCategories;
    @OneToMany(mappedBy = "society", cascade = {}, fetch = FetchType.LAZY)
    private List<Post> posts;
    @ManyToMany(mappedBy = "societies", cascade = {}, fetch = FetchType.LAZY)
    private List<Staff> staffs;   
    @OneToMany(mappedBy = "society", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Event> events;
    @OneToMany(mappedBy = "society", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<FeedbackSurvey> surveys;
    
    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    @JoinTable(name = "memberStudents", joinColumns = @JoinColumn(name = "society_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Student> memberStudents;
    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    @JoinTable(name = "followedStudents", joinColumns = @JoinColumn(name = "society_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<Student> followedStudents;
    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    @JoinTable(name = "leaderStudents", joinColumns = @JoinColumn(name = "society_id"), inverseJoinColumns = @JoinColumn(name = "leader_id"))
    private List<Student> leaderStudents;
    
    @OneToMany(mappedBy = "society", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Attendance> attendances;
    
    public Society() {
        this.announcements = new ArrayList<>();
        this.societyCategories = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.staffs = new ArrayList<>();
        this.events = new ArrayList<>();
        this.memberStudents = new ArrayList<>();
        this.leaderStudents = new ArrayList<>();
        this.followedStudents = new ArrayList<>();
        this.surveys = new ArrayList<>();
        this.attendances = new ArrayList<>();
    }

    public Society(String name, String description, Date dateCreated) {
        this();
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }
    
    public Society(String name, String description, Date dateCreated, String profilePicture) {
        this();
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.profilePicture = profilePicture;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public List<Student> getMemberStudents() {
        return memberStudents;
    }

    public void setMemberStudents(List<Student> memberStudents) {
        this.memberStudents = memberStudents;
    }
    
    public List<Student> getLeaderStudents() {
        return leaderStudents;
    }

    public void setLeaderStudents(List<Student> leaderStudents) {
        this.leaderStudents = leaderStudents;
    }

    public List<Student> getFollowedStudents() {
        return followedStudents;
    }

    public void setFollowedStudents(List<Student> followedStudents) {
        this.followedStudents = followedStudents;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (societyId != null ? societyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the societyId fields are not set
        if (!(object instanceof Society)) {
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
        return this.name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<FeedbackSurvey> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<FeedbackSurvey> surveys) {
        this.surveys = surveys;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }  
}