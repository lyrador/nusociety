/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import entity.EventCategory; 
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
/**
 *
 * @author ajayan
 */
@FacesConverter(value = "eventCategoryConverter", forClass = EventCategory.class)
public class EventCategoryConverter implements Converter{
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {

        if (value == null || value.length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {
            Long objLong = Long.parseLong(value);
            
            List<EventCategory> eventCategories = (List<EventCategory>)context.getExternalContext().getSessionMap().get("EventManagementManagedBean.categories");
            
            for(EventCategory eventCategory:eventCategories)
            {
                if(eventCategory.getEventCategoryId().equals(objLong))
                {
                    return eventCategory;
                }
            }
        }
        catch(Exception ex)
        {
            throw new IllegalArgumentException("Please select a valid value");
        }
        
        return null;
    }

    
    
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null) 
        {
            return "";
        }
        
        if (value instanceof String)
        {
            return "";
        }

        
        
        if (value instanceof EventCategory)
        {
            EventCategory eventCategory = (EventCategory) value;                        
            
            try
            {
                return eventCategory.getEventCategoryId().toString();
            }
            catch(Exception ex)
            {
                throw new IllegalArgumentException("Invalid value");
            }
        }
        else 
        {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}