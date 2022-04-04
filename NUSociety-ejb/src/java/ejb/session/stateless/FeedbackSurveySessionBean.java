/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FeedbackSurvey;
import entity.Society;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Stateless
public class FeedbackSurveySessionBean implements FeedbackSurveySessionBeanLocal {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Long submitSurvey(FeedbackSurvey survey, Long societyId) throws SocietyNotFoundException {
        Society society = (Society) societySessionBeanLocal.retrieveSocietyById(societyId);
        survey.setSociety(society);
        society.getSurveys().add(survey);
        
        em.persist(survey);
        em.flush();
        
        return survey.getFeedbackSurveyId();
    } 

}
