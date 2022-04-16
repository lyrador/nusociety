/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
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
import util.exception.CreateSocietyException;
import util.exception.DeleteSocietyCategoryException;
import util.exception.SocietyCategoryNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateSocietyReq;

/**
 * REST Web Service
 *
 * @author raihan
 */
@Path("Society")
public class SocietyResource {

    SocietySessionBeanLocal societySessionBeanLocal = lookupSocietySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SocietyResource
     */
    public SocietyResource() {
    }

    @Path("retrieveAllSocieties")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSocieties() {
        try {
            List<Society> societies = societySessionBeanLocal.retrieveAllSocieties();
            
            for (Society society : societies) {
                society.getSocietyCategories().clear();
                society.getStaffs().clear();
                society.getSurveys().clear();
                society.getAnnouncements().clear();
                society.getFollowedStudents().clear();
                society.getMemberStudents().clear();
                society.getPosts().clear();
                society.getEvents().clear();
            }
            GenericEntity<List<Society>> genericEntity = new GenericEntity<List<Society>>(societies) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllSocietiesForStaff/{staffId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSocietiesForStaff(@PathParam("staffId") Long staffId) {
        try {
            List<Society> societies = societySessionBeanLocal.retrieveSocietiesForStaff(staffId);
            
            for (Society society : societies) {
                society.getSocietyCategories().clear();
                society.getStaffs().clear();
                society.getSurveys().clear();
                society.getAnnouncements().clear();
                society.getFollowedStudents().clear();
                society.getMemberStudents().clear();
                society.getPosts().clear();
                society.getEvents().clear();
                society.getAttendances().clear();
                society.getLeaderStudents().clear();
            }
            GenericEntity<List<Society>> genericEntity = new GenericEntity<List<Society>>(societies) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSociety/{societyId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSocietyBySocietyId(@PathParam("societyId") Long societyId) {
        try {
            Society society = societySessionBeanLocal.retrieveSocietyById(societyId);
            
            society.getSocietyCategories().clear();
            society.getStaffs().clear();
            society.getSurveys().clear();
            society.getAnnouncements().clear();
            society.getFollowedStudents().clear();
            society.getMemberStudents().clear();
            society.getPosts().clear();
            society.getEvents().clear();
            
            return Response.status(Status.OK).entity(society).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewSociety(CreateSocietyReq createSocietyReq) {
        if (createSocietyReq != null) {
            try {
                Society society = societySessionBeanLocal.createNewSociety(createSocietyReq.getSociety(), createSocietyReq.getSocietyCategoryIds(), createSocietyReq.getStaffIds());

                return Response.status(Response.Status.OK).entity(society.getSocietyId()).build();
            } catch (CreateSocietyException | UnknownPersistenceException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } 
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new society request").build();
        }
    }
    
    @Path("{societyId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("societyId") Long societyId)
    {
        try
        {
            societySessionBeanLocal.deleteSociety(societyId);
            
            return Response.status(Status.OK).build();
        }
        catch(SocietyNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

    private SocietySessionBeanLocal lookupSocietySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SocietySessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/SocietySessionBean!ejb.session.stateless.SocietySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
   



}
