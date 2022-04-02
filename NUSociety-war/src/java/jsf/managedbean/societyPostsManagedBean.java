/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PostSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Comment;
import entity.Post;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "societyPostsManagedBean")
@ViewScoped
public class societyPostsManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @Inject
    private SocietyManagedBean societyManagedBean;

    private List<Post> postList;

    private Society currentSociety;

    private Post postToDelete;

    private Post postToView;
    private List<Comment> commentsOfPost;

    private Post postToUpdate;

    public societyPostsManagedBean() {
    }

    @PostConstruct
    public void PostConstruct() {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            currentSociety = societyManagedBean.getSociety();

            studentFeed(currentStudent);
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        //postList = societyManagedBean.getSociety().getPosts();

    }

    public void studentFeed(Student currentStudent) {
        if (postList == null) {
            postList = new ArrayList<>();
        } else {
            postList.clear();
        }

        postList = societyManagedBean.getSociety().getPosts();
        System.out.println(postList.size());

        //If student is not a member, remove private
        if (!currentStudent.getMemberSocieties().contains(currentSociety)) {
            Iterator<Post> i = postList.iterator();
            while (i.hasNext()) {
                Post p = i.next();
                if (!p.isPostIsPublic()) {
                    i.remove();
                }
            }
        }
        System.out.println(postList.size());

        postList.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
        });

    }

    public void sortByLatest(ActionEvent event) {
        postList.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
        });
    }

    public void sortByEarliest(ActionEvent event) {
        postList.sort((p1, p2) -> {
            return p1.getCreationDate().compareTo(p2.getCreationDate());
        });
    }

    public void doViewSocietyPagePost(ActionEvent event) {
        postToView = (Post) event.getComponent().getAttributes().get("postToView");
        commentsOfPost = postToView.getComments();
        commentsOfPost.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
        });
    }

    public void deleteSocietyPagePost(ActionEvent event) {

        try {
            postToDelete = (Post) event.getComponent().getAttributes().get("postToDelete");
            System.out.println(postToDelete.getPostId() + "THIS IS THE ID");
            postSessionBean.deletePost(postToDelete.getPostId());

            Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());

            studentFeed(currentStudent);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post Deleted successfully", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateSocietyPagePost(ActionEvent event) {
        postToUpdate = (Post) event.getComponent().getAttributes().get("postToUpdate");
    }
    
    public void updateSocietyPagePost(ActionEvent event) {
        try {
             System.out.println(postList.size());
            postSessionBean.updatePost(postToUpdate.getPostId(), postToUpdate);

            Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());

            studentFeed(currentStudent);
            
            System.out.println(postList.size());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post updated successfully! ", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public Post getPostToDelete() {
        return postToDelete;
    }

    public void setPostToDelete(Post postToDelete) {
        this.postToDelete = postToDelete;
    }

    public Post getPostToView() {
        return postToView;
    }

    public void setPostToView(Post postToView) {
        this.postToView = postToView;
    }

    public Post getPostToUpdate() {
        return postToUpdate;
    }

    public void setPostToUpdate(Post postToUpdate) {
        this.postToUpdate = postToUpdate;
    }

    public List<Comment> getCommentsOfPost() {
        return commentsOfPost;
    }

    public void setCommentsOfPost(List<Comment> commentsOfPost) {
        this.commentsOfPost = commentsOfPost;
    }

}
