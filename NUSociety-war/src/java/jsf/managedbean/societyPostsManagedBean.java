/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PostSessionBeanLocal;
import entity.Comment;
import entity.Post;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "societyPostsManagedBean")
@ViewScoped
public class societyPostsManagedBean implements Serializable {

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @Inject
    private SocietyManagedBean societyManagedBean;
    
    private List<Post> postList;

    private Post postToDelete;

    private Post postToView;
    private List<Comment> commentsOfPost;

    private Post postToUpdate;

    public societyPostsManagedBean() {
    }

    @PostConstruct
    public void PostConstruct() {
        
        postList = societyManagedBean.getSociety().getPosts();
        
        //System.out.println("DONE");
               // System.out.println(societyManagedBean.getSociety().getName());

        
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
