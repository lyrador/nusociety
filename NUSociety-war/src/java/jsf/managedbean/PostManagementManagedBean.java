/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PostSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Post;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "postManagementManagedBean")
@SessionScoped
public class PostManagementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    //Inject SocietyBean
    private Post newPostEntity;
    private Long newStudentId;
    private Long newSocietyId;

    private Post postToUpdate;
    private Long postUpdateId;
    
    private Long postDeleteId;

    private List<Post> listOfPosts;

    public PostManagementManagedBean() {
        newPostEntity = new Post();
        postToUpdate = new Post();
    }

    @PostConstruct
    public void PostConstruct() {
        listOfPosts = postSessionBean.retrieveAllPostsInDatabase();
    }

    public void createNewPost(ActionEvent event) throws StudentNotFoundException {
        try {
        newPostEntity.setStudent(studentSessionBean.retrieveStudentByStudentId(newStudentId));
        newPostEntity.setSociety(postSessionBean.retrieveSocietyById(newSocietyId));
        
        Long pId = postSessionBean.createNewPost(newPostEntity);
        newPostEntity = new Post();
        newSocietyId = null;
        newStudentId = null;
        listOfPosts = postSessionBean.retrieveAllPostsInDatabase();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Post created successfully (Post ID: " + pId + ")", null));
        } catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Post: " + ex.getMessage(), null));
        }

    }

    public void doUpdatePost(ActionEvent event) {
        try {
            postSessionBean.updatePost(postUpdateId, postToUpdate);
            listOfPosts = postSessionBean.retrieveAllPostsInDatabase();
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post updated successfully! ", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }

    public void deletePost(ActionEvent event) {
        try {
            //Post commentToDelete = (Post) event.getComponent().getAttributes().get("postToDelete");
            postSessionBean.deletePost(postDeleteId);
            listOfPosts = postSessionBean.retrieveAllPostsInDatabase();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post Deleted successfully", null));

        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public Post getNewPostEntity() {
        return newPostEntity;
    }

    public void setNewPostEntity(Post newPostEntity) {
        this.newPostEntity = newPostEntity;
    }

    public Long getNewStudentId() {
        return newStudentId;
    }

    public void setNewStudentId(Long newStudentId) {
        this.newStudentId = newStudentId;
    }

    public Long getNewSocietyId() {
        return newSocietyId;
    }

    public void setNewSocietyId(Long newSocietyId) {
        this.newSocietyId = newSocietyId;
    }

    public Post getPostToUpdate() {
        return postToUpdate;
    }

    public void setPostToUpdate(Post postToUpdate) {
        this.postToUpdate = postToUpdate;
    }

    public List<Post> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(List<Post> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

    public Long getPostDeleteId() {
        return postDeleteId;
    }

    public void setPostDeleteId(Long postDeleteId) {
        this.postDeleteId = postDeleteId;
    }

    //Can delete next time
    public Long getPostUpdateId() {
        return postUpdateId;
    }

    public void setPostUpdateId(Long postUpdateId) {
        this.postUpdateId = postUpdateId;
    }
    
    
}
