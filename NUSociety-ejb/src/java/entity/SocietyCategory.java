/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author raihan
 */
@Entity
public class SocietyCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long societyCategoryId;
    
    @Column(nullable = false)
    private String societyCategoryName;
    
    @ManyToMany(mappedBy = "societyCategories", cascade = {}, fetch = FetchType.LAZY)
    private List<Society> societies;

    public SocietyCategory() {
        this.societies = new ArrayList<>();
    }

    public SocietyCategory(String societyCategoryName) {
        this.societyCategoryName = societyCategoryName;
    }


    public Long getSocietyCategoryId() {
        return societyCategoryId;
    }

    public void setSocietyCategoryId(Long societyCategoryId) {
        this.societyCategoryId = societyCategoryId;
    }

    public String getSocietyCategoryName() {
        return societyCategoryName;
    }

    public void setSocietyCategoryName(String societyCategoryName) {
        this.societyCategoryName = societyCategoryName;
    }

    @JsonbTransient
    public List<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(List<Society> societies) {
        this.societies = societies;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (societyCategoryId != null ? societyCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the societyCategoryId fields are not set
        if (!(object instanceof SocietyCategory)) {
            return false;
        }
        SocietyCategory other = (SocietyCategory) object;
        if ((this.societyCategoryId == null && other.societyCategoryId != null) || (this.societyCategoryId != null && !this.societyCategoryId.equals(other.societyCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return societyCategoryName;
    }
    
}
