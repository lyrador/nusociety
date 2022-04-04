/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FeedbackSurveySessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import entity.FeedbackSurvey;
import entity.Society;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.SocietyNotFoundException;

/**
 *
 * @author raihan
 */
@Named(value = "feedbackSurveyManagedBean")
@RequestScoped
public class FeedbackSurveyManagedBean {

    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;

    @EJB
    private FeedbackSurveySessionBeanLocal feedbackSurveySessionBeanLocal;

    private FeedbackSurvey survey;
    private Society currentSociety;
    
    public FeedbackSurveyManagedBean() {
        this.survey = new FeedbackSurvey();
    }
    
    @PostConstruct
    public void init() {
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        try {
            this.currentSociety = societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId));
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
    }
    
    public void submitSurvey(ActionEvent event) {
        
        try {
            survey.setDate(new Date());
            System.out.println("title here" + survey.getTitle());
            Long id = feedbackSurveySessionBeanLocal.submitSurvey(survey, currentSociety.getSocietyId());
            System.out.println("**** id is" + id);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Feedback survey submitted! (Feedback Survey ID: " + id + ")", null));

        } catch (SocietyNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while submitting the survey: " + ex.getMessage(), null));

        }
        
    }

    public FeedbackSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(FeedbackSurvey survey) {
        this.survey = survey;
    }

    public Society getCurrentSociety() {
        return currentSociety;
    }

    public void setCurrentSociety(Society currentSociety) {
        this.currentSociety = currentSociety;
    }
    
}
