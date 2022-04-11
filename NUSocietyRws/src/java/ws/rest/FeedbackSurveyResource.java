/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.FeedbackSurveySessionBeanLocal;
import ejb.session.stateless.SocietyCategorySessionBeanLocal;
import entity.FeedbackSurvey;
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
@Path("FeedbackSurvey")
public class FeedbackSurveyResource {

    FeedbackSurveySessionBeanLocal feedbackSurveySessionBeanLocal = lookupFeedbackSurveySessionBeanLocal();

    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SocietyCategoryResource
     */
    public FeedbackSurveyResource() {
    }

    @Path("retrieveFeedbackSurveysForStaff/{staffId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFeedbackSurveysForYourSocieties(@PathParam("staffId") Long staffId) {
        try {
            List<FeedbackSurvey> surveys = feedbackSurveySessionBeanLocal.retrieveFeedbackSurveysForStaffSociety(staffId);
            
            GenericEntity<List<FeedbackSurvey>> genericEntity = new GenericEntity<List<FeedbackSurvey>>(surveys) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }


    private FeedbackSurveySessionBeanLocal lookupFeedbackSurveySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FeedbackSurveySessionBeanLocal) c.lookup("java:global/NUSociety/NUSociety-ejb/FeedbackSurveySessionBean!ejb.session.stateless.FeedbackSurveySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
