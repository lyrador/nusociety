/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Post;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author 65912
 */
@Named(value = "viewPostManagedBean")
@RequestScoped
public class ViewPostManagedBean implements Serializable {

    private Post viewPost;
    
    public ViewPostManagedBean() {
        viewPost = new Post();
    }
    
    public void viewAPost(ActionEvent event) {
        viewPost = (Post) event.getComponent().getAttributes().get("viewAPost");
    }

    public Post getViewPost() {
        return viewPost;
    }

    public void setViewPost(Post viewPost) {
        this.viewPost = viewPost;
    }

}
