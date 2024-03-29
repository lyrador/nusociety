/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FeedbackSurvey;
import java.util.List;
import javax.ejb.Local;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface FeedbackSurveySessionBeanLocal {

    public Long submitSurvey(FeedbackSurvey survey, Long societyId) throws SocietyNotFoundException;

    public List<FeedbackSurvey> retrieveFeedbackSurveysForStaffSociety(Long staffId);
    
}
