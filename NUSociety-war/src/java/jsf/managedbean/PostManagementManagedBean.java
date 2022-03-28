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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Named(value = "postManagementManagedBean")
@ViewScoped
public class PostManagementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    //@Inject
    //private FileUploadManagedBean fileUploadSessionBean;
    //Inject SocietyBean
    private Post newPostEntity;
    private List<Society> followedSociety;
    private List<Society> memberSociety;
    private Society newS;


    private Post postToUpdate;

    private Post postToView;
    private List<Comment> commentsOfPost;
    private boolean viewComments;

    private List<Post> listOfPosts;

    public PostManagementManagedBean() {
        //Get Customer from session scope then get post
        newPostEntity = new Post();
        postToUpdate = new Post();
        viewComments = false;
        newS = new Society();
    }

    @PostConstruct
    public void PostConstruct() {
        //Currently this is for student's OWN posts
        Student currentStudent =  (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        
        listOfPosts = currentStudent.getPosts(); 
        listOfPosts.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
        });
        
        followedSociety = currentStudent.getFollowedSocieties();
        
        memberSociety = currentStudent.getMemberSocieties();
        
        for (int i = 0; i < memberSociety.size(); i++) {
            if (!followedSociety.contains(memberSociety.get(i))) {
                followedSociety.add(memberSociety.get(i));
            }
        }
        
        
    }
    
    public void sortByLatest(ActionEvent event) {
        listOfPosts.sort((p1, p2) -> {
            return p2.getCreationDate().compareTo(p1.getCreationDate());
        });
    }
    
    public void sortByEarliest(ActionEvent event) {
        listOfPosts.sort((p1, p2) -> {
            return p1.getCreationDate().compareTo(p2.getCreationDate());
        });
    }

    public void createNewPost(ActionEvent event) throws StudentNotFoundException {
            Student currentStudent =  (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            newPostEntity.setStudent(currentStudent);

            newPostEntity.setSociety(newS);
           // String publicOrPriv = newPostEntity.ge
            
            Long pId = postSessionBean.createNewPost(newPostEntity);
            newPostEntity = new Post();
            newS = new Society();
            
            listOfPosts = currentStudent.getPosts(); 

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Post created successfully (Post ID: " + pId + ")", null));

    }

    public void doUpdatePost(ActionEvent event) {
        postToUpdate = (Post) event.getComponent().getAttributes().get("postToUpdate");
        System.out.println("CONTENT = " + postToUpdate.getBodyContent());

    }

    public void viewPostComments(ActionEvent event) {
        viewComments = true;
    }

    public void updatePost(ActionEvent event) {
        try {
            postSessionBean.updatePost(postToUpdate.getPostId(), postToUpdate);

            Student currentStudent =  (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            listOfPosts = currentStudent.getPosts(); 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post updated successfully! ", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deletePost(ActionEvent event) {
        try {
            Post postToDelete = (Post) event.getComponent().getAttributes().get("postToDelete");
            postSessionBean.deletePost(postToDelete.getPostId());

            Student currentStudent =  (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            listOfPosts = currentStudent.getPosts(); 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post Deleted successfully", null));
        } catch (PostNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Post: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void viewPost(ActionEvent event) {
        postToView = (Post) event.getComponent().getAttributes().get("selectedPost");
        commentsOfPost = postToView.getComments();
        viewComments = false;
    }


    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("filename");
          //  return new DefaultStreamedContent(new FileInputStream(new File(filename)));
          return null; 
        }
    }

    public Post getNewPostEntity() {
        return newPostEntity;
    }

    public void setNewPostEntity(Post newPostEntity) {
        this.newPostEntity = newPostEntity;
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

    public Post getPostToView() {
        return postToView;
    }

    public void setPostToView(Post postToView) {
        this.postToView = postToView;
    }

    public List<Comment> getCommentsOfPost() {
        return commentsOfPost;
    }

    public void setCommentsOfPost(List<Comment> commentsOfPost) {
        this.commentsOfPost = commentsOfPost;
    }

    public boolean isViewComments() {
        return viewComments;
    }

    public void setViewComments(boolean viewComments) {
        this.viewComments = viewComments;
    }

    public List<Society> getFollowedSociety() {
        return followedSociety;
    }

    public void setFollowedSociety(List<Society> followedSociety) {
        this.followedSociety = followedSociety;
    }

    public List<Society> getMemberSociety() {
        return memberSociety;
    }

    public void setMemberSociety(List<Society> memberSociety) {
        this.memberSociety = memberSociety;
    }

    public Society getNewS() {
        return newS;
    }

    public void setNewS(Society newS) {
        this.newS = newS;
    }

}
