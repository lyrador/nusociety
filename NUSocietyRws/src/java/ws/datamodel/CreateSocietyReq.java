package ws.datamodel;


import entity.Society;
import java.util.ArrayList;
import java.util.List;



public class CreateSocietyReq
{
//    private String username;
//    private String password;
    private Society society;
    private List<Long> societyCategoryIds;
    private List<Long> staffIds;

    
    
    public CreateSocietyReq()
    {        
        this.staffIds = new ArrayList<>();
    }

    
    
    public CreateSocietyReq(Society society, List<Long> societyCategoryIds, List<Long> staffIds) 
    {
        this.society = society;
        this.societyCategoryIds = societyCategoryIds;
        this.staffIds = staffIds;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public List<Long> getSocietyCategoryIds() {
        return societyCategoryIds;
    }

    public void setSocietyCategoryIds(List<Long> societyCategoryIds) {
        this.societyCategoryIds = societyCategoryIds;
    }

    public List<Long> getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(List<Long> staffIds) {
        this.staffIds = staffIds;
    }
    
    
    
}