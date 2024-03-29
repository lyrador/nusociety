/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author yeeda
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.rest.CommentResource.class);
        resources.add(ws.rest.CorsFilter.class);
        resources.add(ws.rest.EventCategoryResource.class);
        resources.add(ws.rest.FeedbackSurveyResource.class);
        resources.add(ws.rest.PostResource.class);
        resources.add(ws.rest.SocietyCategoryResource.class);
        resources.add(ws.rest.SocietyResource.class);
        resources.add(ws.rest.StaffResource.class);
        resources.add(ws.rest.StudentResource.class);
    }
    
}
