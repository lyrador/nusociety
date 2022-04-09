/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Staff;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author yeeda
 */
@Path("Staff")
public class StaffResource {

    StaffSessionBeanLocal staffSessionBeanLocal = lookupStaffSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StaffResource
     */
    public StaffResource() {
    }

    @Path("retrieveAllStaffs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllStaffs() {
        try {
            List<Staff> staffs = staffSessionBeanLocal.retrieveAllStaffs();
            GenericEntity<List<Staff>> genericEntity = new GenericEntity<List<Staff>>(staffs) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewStaff(Staff newStaff) {
        if (newStaff != null) {
            try {
//                newStaff.setUserName("12345");
                System.out.println(newStaff.getUsername());
                System.out.println(newStaff.getEmail());
                Long newStaffId = staffSessionBeanLocal.createNewStaff(newStaff);

                return Response.status(Response.Status.OK).entity(newStaffId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new staff request").build();
        }
    }

    private StaffSessionBeanLocal lookupStaffSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StaffSessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/StaffSessionBean!ejb.session.stateless.StaffSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    @Path("staffLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response staffLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password)
    {
        try
        {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Staff " + staff.getUsername() + " login remotely via web service");

            staff.setPassword(null);       
            
            return Response.status(Status.OK).entity(staff).build();
        }
//        catch(InvalidLoginCredentialException ex)
//        {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
