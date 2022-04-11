/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import entity.Society;
import entity.SocietyCategory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.DeleteSocietyCategoryException;
import util.exception.SocietyCategoryNotFoundException;

/**
 * REST Web Service
 *
 * @author raihan
 */
@Path("SocietyCategory")
public class SocietyCategoryResource {

    SocietyCategorySessionBeanLocal societyCategorySessionBeanLocal = lookupSocietyCategorySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SocietyCategoryResource
     */
    public SocietyCategoryResource() {
    }

    @Path("retrieveAllSocietyCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSocietyCategories() {
        try {
            List<SocietyCategory> categories = societyCategorySessionBeanLocal.retrieveAllSocietyCategories();
            
            for (SocietyCategory c : categories) {
                c.getSocieties().size();
            }
            GenericEntity<List<SocietyCategory>> genericEntity = new GenericEntity<List<SocietyCategory>>(categories) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewSocietyCategory(SocietyCategory newCategory) {
        if (newCategory != null) {
            try {
                SocietyCategory category = societyCategorySessionBeanLocal.createNewSocietyCategory(newCategory);

                return Response.status(Response.Status.OK).entity(category.getSocietyCategoryId()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new society category request").build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSocietyCategory(SocietyCategory newCategory) {
        if (newCategory != null) {
            try {
                societyCategorySessionBeanLocal.updateSocietyCategory(newCategory);

                return Response.status(Status.OK).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new society category request").build();
        }
    }
    
    @Path("{societyCategoryId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSocietyCategory(@PathParam("societyCategoryId") Long societyCategoryId) {
        
        System.out.println(societyCategoryId);
        try {
            societyCategorySessionBeanLocal.deleteSocietyCategory(societyCategoryId);
            
            return Response.status(Status.OK).build();
        }
        catch(SocietyCategoryNotFoundException | DeleteSocietyCategoryException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private SocietyCategorySessionBeanLocal lookupSocietyCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SocietyCategorySessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/SocietyCategorySessionBean!ejb.session.stateless.SocietyCategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
