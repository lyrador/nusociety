/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import entity.Society;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;




@FacesConverter(value = "societyConverter", forClass = Society.class)

public class SocietyConverter implements Converter 
{
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {

        if (value == null || value.length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {
            Long objLong = Long.parseLong(value);
            System.out.println("********* " + value + "*********");
            System.out.println("********* " + objLong + "*********");
            
            List<Society> societies = (List<Society>)context.getExternalContext().getSessionMap().get("EventManagementManagedBean.societies");
            
            for(Society society:societies)
            {
                if(society.getSocietyId().equals(objLong))
                {
                    System.out.println("********* " + society.getName() + " *********");
                    return society;
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

        
        
        if (value instanceof Society)
        {
            Society society = (Society) value;                        
            
            try
            {
                return society.getSocietyId().toString();
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