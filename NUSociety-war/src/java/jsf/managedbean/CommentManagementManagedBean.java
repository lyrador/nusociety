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
import javax.inject.Inject;
//import javax.faces.view.ViewScoped;
import util.exception.CommentNotFoundException;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "createNewCommentManagedBean")
@ViewScoped
public class CommentManagementManagedBean implements Serializable {

    @EJB
    private PostSessionBeanLocal postSessionBean;
    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    @EJB
    private CommentSessionBeanLocal commentSessionBean;
    
    @Inject
    private PostManagementManagedBean postManagementManagedBean;

    private Comment newCommentEntity;
    //private Long newStudentId;
    //private Long newPostId;

    private String contentUpdate;
    private Long commentUpdateId;
    
    private Long commentDeleteId;
    
    private List<Comment> commentOfPost;
    
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
        
        //Student Id should take from Session Scope!!!
        System.out.println("TEST!!!");
        Post postOfNewComment = (Post) event.getComponent().getAttributes().get("postOfNewComment");
        newCommentEntity.setStudent(postOfNewComment.getStudent());
        newCommentEntity.setPost(postOfNewComment);
        newCommentEntity.setCreationDate(new Date());
        
        Long cId = commentSessionBean.createComment(newCommentEntity);
        postManagementManagedBean.getCommentsOfPost().add(newCommentEntity);
        newCommentEntity = new Comment();
        
        //listOfComments = commentSessionBean.viewAllCommentsInDatabase();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Comment created successfully (Comment ID: " + cId + ")", null));
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
    
    public void findCommentOfPost(ActionEvent event) {
        Post selectedPost = (Post) event.getComponent().getAttributes().get("selectedPost");
        commentOfPost = selectedPost.getComments();
    }

    public Comment getNewCommentEntity() {
        return newCommentEntity;
    }

    public void setNewCommentEntity(Comment newCommentEntity) {
        this.newCommentEntity = newCommentEntity;
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

    public List<Comment> getCommentOfPost() {
        return commentOfPost;
    }

    public void setCommentOfPost(List<Comment> commentOfPost) {
        this.commentOfPost = commentOfPost;
    }
    
    

}
