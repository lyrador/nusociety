/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FeedbackSurvey;
import entity.Society;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    @Override
    public List<FeedbackSurvey> retrieveFeedbackSurveysForStaffSociety(Long staffId) {
        
        Query query = em.createQuery("SELECT fs FROM FeedbackSurvey fs, IN (fs.society.staffs) s WHERE s.staffId = :inStaffId");
        query.setParameter("inStaffId", staffId);
        
        List<FeedbackSurvey> surveys = query.getResultList();
        
        return surveys;
    }

}
