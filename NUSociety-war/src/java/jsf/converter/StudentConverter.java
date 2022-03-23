/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import entity.Student; 
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
/**
 *
 * @author ajayan
 */
@FacesConverter(value = "studentConverter", forClass = Student.class)

public class StudentConverter implements Converter{
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {

        if (value == null || value.length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {
            Long objLong = Long.parseLong(value);
            
            List<Student> students = (List<Student>)context.getExternalContext().getSessionMap().get("EventManagementManagedBean.students");
            
            for(Student student:students)
            {
                if(student.getStudentId().equals(objLong))
                {
                    return student;
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

        
        
        if (value instanceof Student)
        {
            Student student = (Student) value;                        
            
            try
            {
                return student.getStudentId().toString();
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
