/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Society;
import entity.Student;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateStudentReq;
import ws.datamodel.MakeStudentLeaderReq;

/**
 * REST Web Service
 *
 * @author yeeda
 */
@Path("Student")
public class StudentResource {

    StudentSessionBeanLocal studentSessionBeanLocal = lookupStudentSessionBeanLocal();
    SocietySessionBeanLocal societySessionBeanLocal = lookupSocietySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StudentResource
     */
    public StudentResource() {
    }

    @Path("retrieveAllStudents")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllStudents() {
        try {
            List<Student> students = studentSessionBeanLocal.retrieveAllStudents();
            GenericEntity<List<Student>> genericEntity = new GenericEntity<List<Student>>(students) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewStudent(Student newStudent) {
        if (newStudent != null) {
            try {
                Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);

                return Response.status(Response.Status.OK).entity(newStudentId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new student request").build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeStudentLeader(MakeStudentLeaderReq makeStudentLeaderReq) {
        if (makeStudentLeaderReq != null) {
            try {
                studentSessionBeanLocal.setStudentLeaderOfSociety(makeStudentLeaderReq.getStudent().getStudentId(), Long.parseLong(makeStudentLeaderReq.getSocietyIdString()));

                return Response.status(Response.Status.OK).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } 
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid make student leader request").build();
        }
    }
    
    @Path("unlink")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlinkStudentLeaderFromSociety(MakeStudentLeaderReq makeStudentLeaderReq) {
        if (makeStudentLeaderReq != null) {
            try {
                studentSessionBeanLocal.unlinkStudentLeaderFromSociety(makeStudentLeaderReq.getStudent().getStudentId(), Long.parseLong(makeStudentLeaderReq.getSocietyIdString()));

                return Response.status(Response.Status.OK).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } 
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid unlink student leader request").build();
        }
    }
    
    @Path("{studentId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("studentId") Long studentId)
    {
        try
        {
            studentSessionBeanLocal.deleteStudent(studentId);
            
            return Response.status(Status.OK).build();
        }
        catch(StudentNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

    private StudentSessionBeanLocal lookupStudentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StudentSessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/StudentSessionBean!ejb.session.stateless.StudentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    @Path("retrieveSocietiesLedByStudent/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSocietiesLedByStudent(@PathParam("studentId") Long studentId) {
        try {
            System.out.println("HELLO");
            List<Society> societies = studentSessionBeanLocal.retrieveSocietiesLedByStudent(studentId);
            
            for (Society society : societies) {
                society.getSocietyCategories().clear();
                society.getStaffs().clear();
                society.getSurveys().clear();
                society.getAnnouncements().clear();
                society.getFollowedStudents().clear();
                society.getMemberStudents().clear();
                society.getLeaderStudents().clear();
                society.getPosts().clear();
                society.getEvents().clear();
                society.getAttendances().clear();
            }
            GenericEntity<List<Society>> genericEntity = new GenericEntity<List<Society>>(societies) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSocietiesStudentIsIn/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSocietiesStudentIsIn(@PathParam("studentId") Long studentId) {
        try {
            System.out.println("HELLO");
            List<Society> societies = studentSessionBeanLocal.retrieveSocietiesForMemberPositions(studentId);
            
            for (Society society : societies) {
                society.getSocietyCategories().clear();
                society.getStaffs().clear();
                society.getSurveys().clear();
                society.getAnnouncements().clear();
                society.getFollowedStudents().clear();
                society.getMemberStudents().clear();
                society.getPosts().clear();
                society.getEvents().clear();
                society.getLeaderStudents().clear();
                society.getAttendances().clear();
            }
            GenericEntity<List<Society>> genericEntity = new GenericEntity<List<Society>>(societies) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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
