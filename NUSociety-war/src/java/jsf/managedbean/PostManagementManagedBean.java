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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
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

    @Inject
    HomePagePostManagedBean homePagePostManagedBean;

    private List<SelectItem> selectItemsFollowedSocietyObject;
    private List<SelectItem> selectItemsFollowedSocietyName;

    private List<SelectItem> selectItemsMemberSocietyObject;
    private List<SelectItem> selectItemsMemberSocietyName;

    private Post newPostEntity;
    private List<Society> followedSociety;
    private List<Society> memberSociety;
    private Society newS;

    private Post postToUpdate;
    
    private Society sortedSociety;

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

        selectItemsFollowedSocietyObject = new ArrayList<>();
        selectItemsFollowedSocietyName = new ArrayList<>();

        selectItemsMemberSocietyObject = new ArrayList<>();
        selectItemsMemberSocietyName = new ArrayList<>();
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isPublic", null);
    }

    @PostConstruct
    public void PostConstruct() {
        //Currently this is for student's OWN posts
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        if(currentStudent != null) {
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
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
            
            //System.out.println

            for (Society society : followedSociety) {
                selectItemsFollowedSocietyObject.add(new SelectItem(society, society.getName()));
                selectItemsFollowedSocietyName.add(new SelectItem(society.getName(), society.getName()));
            }

            for (Society society : memberSociety) {
                selectItemsMemberSocietyObject.add(new SelectItem(society, society.getName()));
                selectItemsMemberSocietyName.add(new SelectItem(society.getName(), society.getName()));
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PostManagementManagedBean.followedSocieties", followedSociety);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PostManagementManagedBean.memberSocieties", memberSociety);

            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isPublic", null);
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        }
    }

    public void studentsPost(Student currentStudent) {
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorted By Latest!", null));
    }

    public void sortByEarliest(ActionEvent event) {
        listOfPosts.sort((p1, p2) -> {
            return p1.getCreationDate().compareTo(p2.getCreationDate());
        });
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorted By Earliest!", null));
    }

    public void sortByPrivate(ActionEvent event) {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            listOfPosts = currentStudent.getPosts();

            Iterator<Post> i = listOfPosts.iterator();
            while (i.hasNext()) {
                Post p = i.next();
                if (p.isPostIsPublic()) {
                    i.remove();
                }

            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Private Posts only!", null));
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorted By Earliest!", null));
    }

    public void sortByPublic(ActionEvent event) {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            listOfPosts = currentStudent.getPosts();
            
            Iterator<Post> i = listOfPosts.iterator();
            while (i.hasNext()) {
                Post p = i.next();
                if (!p.isPostIsPublic()) {
                    i.remove();
                }

            }
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Public Posts only!", null));
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorted By Earliest!", null));
    }

    public void resetFilter(ActionEvent event) {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            listOfPosts = currentStudent.getPosts();
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Filters have been reset!", null));
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void sortBySociety(ActionEvent event) {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        try {
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            listOfPosts = currentStudent.getPosts();
            
            Iterator<Post> i = listOfPosts.iterator();
            while (i.hasNext()) {
                Post p = i.next();
                if (!p.getSociety().equals(sortedSociety)) {
                    i.remove();
                }
            }
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "Posts from " + sortedSociety.getName()+ " only!", null));
        } catch (StudentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorted By Earliest!", null));
    }
    

    public void createNewPost(ActionEvent event) throws StudentNotFoundException {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        newPostEntity.setStudent(currentStudent);
        newPostEntity.setCreationDate(new Date());

        //newPostEntity.setSociety(newS);
        // String publicOrPriv = newPostEntity.ge
        Long pId = postSessionBean.createNewPost(newPostEntity);
        newPostEntity = new Post();
        newS = new Society();

        currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
        studentsPost(currentStudent);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Post created successfully (Post ID: " + pId + ")", null));

    }

    public void createNewPrivatePost(ActionEvent event) throws StudentNotFoundException {
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        newPostEntity.setStudent(currentStudent);
        newPostEntity.setCreationDate(new Date());
        newPostEntity.setPostIsPublic(false);
        //newPostEntity.setSociety(newS);
        // String publicOrPriv = newPostEntity.ge
        Long pId = postSessionBean.createNewPost(newPostEntity);
        newPostEntity = new Post();
        newS = new Society();

        currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
        studentsPost(currentStudent);

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

            Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            studentsPost(currentStudent);
            //homePagePostManagedBean.updatePosts();
            //listOfPosts = currentStudent.getPosts();
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

            Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
            currentStudent = studentSessionBean.retrieveStudentByStudentId(currentStudent.getStudentId());
            studentsPost(currentStudent);
            //homePagePostManagedBean.updatePosts();
            //listOfPosts = currentStudent.getPosts();
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
    
    public void addMessage() {
        String summary = postToUpdate.isPostIsPublic() ? "Public" : "Private";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
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

    public List<SelectItem> getSelectItemsFollowedSocietyObject() {
        return selectItemsFollowedSocietyObject;
    }

    public void setSelectItemsFollowedSocietyObject(List<SelectItem> selectItemsFollowedSocietyObject) {
        this.selectItemsFollowedSocietyObject = selectItemsFollowedSocietyObject;
    }

    public List<SelectItem> getSelectItemsFollowedSocietyName() {
        return selectItemsFollowedSocietyName;
    }

    public void setSelectItemsFollowedSocietyName(List<SelectItem> selectItemsFollowedSocietyName) {
        this.selectItemsFollowedSocietyName = selectItemsFollowedSocietyName;
    }

    public List<SelectItem> getSelectItemsMemberSocietyObject() {
        return selectItemsMemberSocietyObject;
    }

    public void setSelectItemsMemberSocietyObject(List<SelectItem> selectItemsMemberSocietyObject) {
        this.selectItemsMemberSocietyObject = selectItemsMemberSocietyObject;
    }

    public List<SelectItem> getSelectItemsMemberSocietyName() {
        return selectItemsMemberSocietyName;
    }

    public void setSelectItemsMemberSocietyName(List<SelectItem> selectItemsMemberSocietyName) {
        this.selectItemsMemberSocietyName = selectItemsMemberSocietyName;
    }

    public Society getSortedSociety() {
        return sortedSociety;
    }

    public void setSortedSociety(Society sortedSociety) {
        this.sortedSociety = sortedSociety;
    }

}
