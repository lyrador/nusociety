/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author yeeda
 */
@Entity
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, unique = true, length = 128)
    private String email;
    @Column(nullable = false, length = 32)
    private String password;
    @Column(nullable = false, unique = true, length = 32)
    private String userName;
    @Column(nullable = false, unique = true, length = 256)
    private String profilePicture;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AccessRightEnum accessRightEnum;
    
    @OneToMany(mappedBy = "student", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Attendance> attendances;
    @ManyToMany(cascade = {}, fetch = FetchType.LAZY)
    private List<Notification> notifications;
    @OneToMany(mappedBy = "student", cascade = {}, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "student", cascade = {}, fetch = FetchType.LAZY)
    private List<Post> posts;

    public Student() {
        attendances = new ArrayList<>();
        comments = new ArrayList<>();
        posts = new ArrayList<>();
    }

    public Student(String name, String email, String password, String userName, String profilePicture, AccessRightEnum accessRightEnum) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.accessRightEnum = accessRightEnum;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentId != null ? studentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the studentId fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.studentId == null && other.studentId != null) || (this.studentId != null && !this.studentId.equals(other.studentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Student[ id=" + studentId + " ]";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    } 

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }
    
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    
    
}
