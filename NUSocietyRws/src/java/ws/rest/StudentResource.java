/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
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
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateStudentReq;

/**
 * REST Web Service
 *
 * @author yeeda
 */
@Path("Student")
public class StudentResource {

    StudentSessionBeanLocal studentSessionBeanLocal = lookupStudentSessionBeanLocal();

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

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createNewStudent(Student newStudent) {
//        if (newStudent != null) {
//            try {
//                Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);
//
//                return Response.status(Response.Status.OK).entity(newStudentId).build();
//            } catch (Exception ex) {
//                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new student request").build();
//        }
//    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewStudentWithEnum(CreateStudentReq createStudentReq) {
        if (createStudentReq != null) {
            try {
                Long studentId = studentSessionBeanLocal.createNewStudentWithListOfSocietyIdsToBeLeaderOf(createStudentReq.getStudent(), createStudentReq.getListOfSocietyIdsToBeLeaderOf());

                return Response.status(Response.Status.OK).entity(studentId).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } 
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new student request").build();
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
}
