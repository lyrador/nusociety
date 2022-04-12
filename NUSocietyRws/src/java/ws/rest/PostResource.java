/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.PostSessionBeanLocal;
import entity.Post;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbTransient;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.PostNotFoundException;

/**
 * REST Web Service
 *
 * @author 65912
 */
@Path("Post")
public class PostResource {

    PostSessionBeanLocal postSessionBean = lookupPostSessionBeanLocal();

    @Context
    private UriInfo context;

    public PostResource() {
    }

    @Path("retrieveAllPosts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPosts() {
        try {
            List<Post> posts = postSessionBean.retrieveAllPostsInDatabase();

            for (Post post : posts) {
                post.getComments().clear();
                post.setSociety(null);
                post.setStudent(null);
            }

            GenericEntity<List<Post>> genericEntity = new GenericEntity<List<Post>>(posts) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrievePost/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePost(@PathParam("id") Long id) {
        try {
            Post post = postSessionBean.retrievePostById(id);
            post.getComments().clear();
            post.setSociety(null);
            post.setStudent(null);

            return Response.status(Status.OK).entity(post).build();
        } catch (PostNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(Post post) {
        if (post != null) {
            try {
                System.out.println(post.getPostId());
                postSessionBean.updatePost(post.getPostId(), post);
                return Response.status(Response.Status.OK).build();

            } catch (PostNotFoundException ex) {
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
            postSessionBean.deletePost(id);

            return Response.status(Status.OK).build();
        } catch (PostNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private PostSessionBeanLocal lookupPostSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PostSessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/PostSessionBean!ejb.session.stateless.PostSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
