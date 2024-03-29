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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author 65912
 */
@Entity
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false, length = 128)
    private String bodyContent;
    @Column(nullable = false)
    private String image;
    private Date creationDate;
    @Column(nullable = false)
    private boolean postIsPublic;
    
    @OneToMany(mappedBy = "post", cascade = {}, fetch = FetchType.EAGER)
    private List<Comment> comments;  
    
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Student student;   
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Society society;

    public Post() {
        this.creationDate = new Date();
        this.postIsPublic = true;
        comments = new ArrayList<Comment>();
    }

    public Post(String bodyContent, String image, Student student, Society society) {
        this();
        this.bodyContent = bodyContent;
        this.image = image;
        this.student = student;
        this.society = society;
        this.creationDate = new Date();
        this.postIsPublic = true;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isPostIsPublic() {
        return postIsPublic;
    }

    public void setPostIsPublic(boolean postIsPublic) {
        this.postIsPublic = postIsPublic;
    }
    
    public String getPublicOrPrivate() {
        if (postIsPublic) {
            return "Public";
        } else {
            return "Private";
        }
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (postId != null ? postId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the postId fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.postId == null && other.postId != null) || (this.postId != null && !this.postId.equals(other.postId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Post[ id=" + postId + " ]";
    }
    
    
}
