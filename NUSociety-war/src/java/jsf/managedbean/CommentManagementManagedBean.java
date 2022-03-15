/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CommentSessionBeanLocal;
import ejb.session.stateless.PostSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Comment;
import entity.Post;
import entity.Student;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
//import javax.faces.view.ViewScoped;
import util.exception.CommentNotFoundException;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "createNewCommentManagedBean")
@SessionScoped
public class CommentManagementManagedBean implements Serializable {

    @EJB
    private PostSessionBeanLocal postSessionBean;
    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    @EJB
    private CommentSessionBeanLocal commentSessionBean;

    private Comment newCommentEntity;
    private Long newStudentId;
    private Long newPostId;

    private String contentUpdate;
    private Long commentUpdateId;
    
    private Long commentDeleteId;
    
    //Next time change to student's own comments
    private List<Comment> listOfComments;

    public CommentManagementManagedBean() {
        newCommentEntity = new Comment();
        //commentToBeUpdated = new Comment();
    }
    
    //Change to student
    @PostConstruct
    public void PostConstruct() {
        listOfComments = commentSessionBean.viewAllCommentsInDatabase();
    }

    public void createNewComment(ActionEvent event) throws StudentNotFoundException {
        try {
            newCommentEntity.setStudent(studentSessionBean.retrieveStudentByStudentId(newStudentId));
            newCommentEntity.setPost(postSessionBean.retrievePostById(newPostId));
            
            newCommentEntity.setCreationDate(new Date());

            Long cId = commentSessionBean.createComment(newCommentEntity);
            newCommentEntity = new Comment();
            newStudentId = null;
            newPostId = null;
            listOfComments = commentSessionBean.viewAllCommentsInDatabase();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Comment created successfully (Comment ID: " + cId + ")", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Comment: " + ex.getMessage(), null));
        }
    }

    //Retrieve Comment from client, Initialise the attributes required for update
    public void doUpdateComment(ActionEvent event) {
        //commentToBeUpdated = (Comment) event.getComponent().getAttributes().get("commentToBeUpdated");
        //creationDateUpdate = commentToBeUpdated.getCreationDate();
        //contentUpdate = commentToBeUpdated.getContent();

    }

    public void updateComment(ActionEvent event) {
        try {
            
            commentSessionBean.updateComment(commentUpdateId,contentUpdate);
            listOfComments = commentSessionBean.viewAllCommentsInDatabase();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment updated successfully", null));
        } catch (CommentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Comment: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }

    public void deleteComment(ActionEvent event) {
        try {
            //Comment commentToDelete = (Comment) event.getComponent().getAttributes().get("commentToDelete");
            commentSessionBean.deleteComment(commentDeleteId);
            commentDeleteId = null;
            listOfComments = commentSessionBean.viewAllCommentsInDatabase();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment Deleted successfully", null));

        } catch (CommentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Comment: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public Comment getNewCommentEntity() {
        return newCommentEntity;
    }

    public void setNewCommentEntity(Comment newCommentEntity) {
        this.newCommentEntity = newCommentEntity;
    }

    public Long getNewStudentId() {
        return newStudentId;
    }

    public void setNewStudentId(Long newStudentId) {
        this.newStudentId = newStudentId;
    }

    public Long getNewPostId() {
        return newPostId;
    }

    public void setNewPostId(Long newPostId) {
        this.newPostId = newPostId;
    }

    public List<Comment> getListOfComments() {
        return listOfComments;
    }

    public void setListOfComments(List<Comment> listOfComments) {
        this.listOfComments = listOfComments;
    }

    public Long getCommentDeleteId() {
        return commentDeleteId;
    }

    public void setCommentDeleteId(Long commentDeleteId) {
        this.commentDeleteId = commentDeleteId;
    }

    public Long getCommentUpdateId() {
        return commentUpdateId;
    }

    public void setCommentUpdateId(Long commentUpdateId) {
        this.commentUpdateId = commentUpdateId;
    }

    public String getContentUpdate() {
        return contentUpdate;
    }

    public void setContentUpdate(String contentUpdate) {
        this.contentUpdate = contentUpdate;
    }
    
    

}
