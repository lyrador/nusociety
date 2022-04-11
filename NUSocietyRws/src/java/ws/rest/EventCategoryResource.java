/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.EventCategorySessionBeanLocal;
import entity.EventCategory;
import entity.Staff;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.EventCategoryNotFoundException;

/**
 * REST Web Service
 *
 * @author ajayan
 */
@Path("EventCategory")
public class EventCategoryResource {

    EventCategorySessionBeanLocal eventCategorySessionBean = lookupEventCategorySessionBeanLocal();
    
    

    @Context
    private UriInfo context;

    
    public EventCategoryResource() {
    }
    
    @Path("retrieveAllEventCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEventCategories() {
        try {
            List<EventCategory> categories = eventCategorySessionBean.retrieveAllEvents();

            for (EventCategory category : categories) {
                category.getEvents().clear();
            }

            GenericEntity<List<EventCategory>> genericEntity = new GenericEntity<List<EventCategory>>(categories) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewEventCategory(EventCategory eventCategory) {
        if (eventCategory != null) {
            try {
                System.out.println(eventCategory.getCategoryName());
                Long eventCategoryId = eventCategorySessionBean.createNewEventCategory(eventCategory); 

                return Response.status(Response.Status.OK).entity(eventCategoryId).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new event category request").build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(EventCategory eventCategory) {
        if (eventCategory != null) {
            try {
                System.out.println(eventCategory.getEventCategoryId());
                eventCategorySessionBean.updateEventCategory(eventCategory, eventCategory.getEventCategoryId());
                return Response.status(Response.Status.OK).build();

            } catch (EventCategoryNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update Post request").build();
        }
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@QueryParam("id") Long id) {
        try {
            eventCategorySessionBean.deleteEventCategory(id);

            return Response.status(Status.OK).build();
        } catch (EventCategoryNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    
    
    private EventCategorySessionBeanLocal lookupEventCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EventCategorySessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/EventCategorySessionBean!ejb.session.stateless.EventCategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
