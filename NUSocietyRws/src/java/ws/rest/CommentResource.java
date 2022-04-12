/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CommentSessionBeanLocal;
import entity.Comment;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CommentNotFoundException;

/**
 * REST Web Service
 *
 * @author 65912
 */
@Path("Comment")
public class CommentResource {

    CommentSessionBeanLocal commentSessionBean = lookupCommentSessionBeanLocal();

    @Context
    private UriInfo context;

    public CommentResource() {
    }

    @Path("retrieveAllComments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllComments() {
        try {
            List<Comment> comments = commentSessionBean.viewAllCommentsInDatabase();

            for (Comment c : comments) {
                //c.getPost().getComments().clear();
                //c.getStudent().getComments().clear();
                c.setPost(null);
                c.setStudent(null);
            }

            GenericEntity<List<Comment>> genericEntity = new GenericEntity<List<Comment>>(comments) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveComment/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveComment(@PathParam("id") Long id) {
        try {
            Comment c = commentSessionBean.retrieveCommentById(id);
            c.setPost(null);
            c.setStudent(null);;

            return Response.status(Status.OK).entity(c).build();
        } catch (CommentNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComment(Comment comment) {
        if (comment != null) {
            try {
 
                commentSessionBean.updateComment(comment.getCommentId(), comment.getContent());
                return Response.status(Response.Status.OK).build();

            } catch (CommentNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update Comment request").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@QueryParam("id") Long id) {
        try {
            commentSessionBean.deleteComment(id);

            return Response.status(Status.OK).build();
        } catch (CommentNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private CommentSessionBeanLocal lookupCommentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CommentSessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/CommentSessionBean!ejb.session.stateless.CommentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
