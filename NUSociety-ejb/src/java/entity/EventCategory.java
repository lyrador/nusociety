/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author ajayan
 */
@Entity
public class EventCategory implements Serializable {

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCategoryId;
    @Column(nullable = false, length = 128)
    private String categoryName; 
    
    @ManyToMany(cascade = {}, fetch = FetchType.LAZY)
    private List<Event> events;

    public Long getEventCategoryId() {
        return eventCategoryId;
    }

    public EventCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public EventCategory() {
    }
    
    

    public void setEventCategoryId(Long eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventCategoryId != null ? eventCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the eventCategoryId fields are not set
        if (!(object instanceof EventCategory)) {
            return false;
        }
        EventCategory other = (EventCategory) object;
        if ((this.eventCategoryId == null && other.eventCategoryId != null) || (this.eventCategoryId != null && !this.eventCategoryId.equals(other.eventCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EventCategory[ id=" + eventCategoryId + " ]";
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
}