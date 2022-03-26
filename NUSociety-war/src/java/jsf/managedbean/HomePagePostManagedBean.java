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
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "homePagePostManagedBean")
@ViewScoped
public class HomePagePostManagedBean implements Serializable {

    @EJB
    private PostSessionBeanLocal postSessionBean;

    private List<Post> postList;

    private Post postToDelete;

    private Post postToView;
    private List<Comment> commentsOfPost;

    private Post postToUpdate;

    public HomePagePostManagedBean() {
    }

    @PostConstruct
    public void PostConstruct() {
        //Currently this is for student's OWN posts
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");

        if (postList == null) {
            postList = new ArrayList<>();
        } else {
            postList.clear();
           
        }

        
        //For followed only add public
        for (Society s : currentStudent.getFollowedSocieties()) {
            for (Post p : s.getPosts()) {
                if (p.isPostIsPublic() && !postList.contains(p)) {
                    postList.add(p);
                }
            }
        }

        for (Society s : currentStudent.getMemberSocieties()) {
            for (Post p : s.getPosts()) {
                if (!postList.contains(p)) {   //Add the remaing private posts
                    postList.add(p);
                }
            }
        }

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

    public void doUpdateHomePagePost(ActionEvent event) {
        postToUpdate = (Post) event.getComponent().getAttributes().get("postToUpdate");
    }

    public void updateHomePagePost(ActionEvent event) {
        try {
            postSessionBean.updatePost(postToUpdate.getPostId(), postToUpdate);

            Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");

            postList.clear();

            for (Society s : currentStudent.getFollowedSocieties()) {
                for (Post p : s.getPosts()) {
                    if (p.isPostIsPublic() && !postList.contains(p)) {
                        postList.add(p);
                    }
                }
            }

            for (Society s : currentStudent.getMemberSocieties()) {
                for (Post p : s.getPosts()) {
                    if (!postList.contains(p)) {   //Add the remaing private posts
                        postList.add(p);
                    }
                }
            }

            postList.sort((p1, p2) -> {
                return p2.getCreationDate().compareTo(p1.getCreationDate());
            });

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post updated successfully! ", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    

    public void doViewHomePagePost(ActionEvent event) {
        postToView = (Post) event.getComponent().getAttributes().get("postToView");
        commentsOfPost = postToView.getComments();
        commentsOfPost.sort((p1, p2) -> {
                return p2.getCreationDate().compareTo(p1.getCreationDate());
            });
    }

    public void deleteHomePagePost(ActionEvent event) {

        try {
            postToDelete = (Post) event.getComponent().getAttributes().get("postToDelete");
            System.out.println(postToDelete.getPostId() + "THIS IS THE ID");
            postSessionBean.deletePost(postToDelete.getPostId());

            //Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            postList.remove(postToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post Deleted successfully", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void updatePosts(ActionEvent event) {
        postList.clear();

        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");

        //For followed only add public
        for (Society s : currentStudent.getFollowedSocieties()) {
            for (Post p : s.getPosts()) {
                if (p.isPostIsPublic() && !postList.contains(p)) {
                    postList.add(p);
                }
            }
        }

        for (Society s : currentStudent.getMemberSocieties()) {
            for (Post p : s.getPosts()) {
                if (!postList.contains(p)) {   //Add the remaing private posts
                    postList.add(p);
                }
            }
        }
        postList.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
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
