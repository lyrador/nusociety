/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author raihan
 */
@Entity
public class FeedbackSurvey implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackSurveyId;
    
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(nullable = false)
    private Integer rating;
    
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Society society;   

    public FeedbackSurvey() {
    }

    public FeedbackSurvey(String title, String content, Date date, Integer rating) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public FeedbackSurvey(String title, String content, Date date, Integer rating, Society society) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.society = society;
    }

    public Long getFeedbackSurveyId() {
        return feedbackSurveyId;
    }

    public void setFeedbackSurveyId(Long feedbackSurveyId) {
        this.feedbackSurveyId = feedbackSurveyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feedbackSurveyId != null ? feedbackSurveyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the feedbackSurveyId fields are not set
        if (!(object instanceof FeedbackSurvey)) {
            return false;
        }
        FeedbackSurvey other = (FeedbackSurvey) object;
        if ((this.feedbackSurveyId == null && other.feedbackSurveyId != null) || (this.feedbackSurveyId != null && !this.feedbackSurveyId.equals(other.feedbackSurveyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FeedbackSurvey[ id=" + feedbackSurveyId + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    
}
