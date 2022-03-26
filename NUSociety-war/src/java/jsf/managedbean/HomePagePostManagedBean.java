/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Post;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author 65912
 */
@Named(value = "homePagePostManagedBean")
@ViewScoped
public class HomePagePostManagedBean implements Serializable {

    private List<Post> postList;

    public HomePagePostManagedBean() {
    }

    @PostConstruct
    public void PostConstruct() {
        //Currently this is for student's OWN posts
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");
        postList = new ArrayList<>();
        
        //For followed only add public
        for (Society s : currentStudent.getFollowedSocieties()) {
            for (Post p : s.getPosts()) {
                if (p.isPostIsPublic()) {
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
          return p1.getCreationDate().compareTo(p2.getCreationDate());
        });
    }
    
    public void updatePosts(ActionEvent event) {
        postList.clear();
        
        Student currentStudent = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStudent");

        //For followed only add public
        for (Society s : currentStudent.getFollowedSocieties()) {
            for (Post p : s.getPosts()) {
                if (p.isPostIsPublic()) {
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
          return p1.getCreationDate().compareTo(p2.getCreationDate());
        });
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
    
    
}
