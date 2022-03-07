/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Post;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Stateless
public class PostSessionBean implements PostSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPost(Post post) {
        em.persist(post);
        em.flush();
        return post.getPostId();
    }
    
    @Override
    public Post retrievePostById(Long postId) throws PostNotFoundException {
        Post p = em.find(Post.class, postId);
        
        if(postId != null) {
            return p; 
        } else {
            throw new PostNotFoundException("Error, Post with ID:" + postId + " not found!");
        }      
    }

    //Update Post
    
    //Delete Post
    
    //Private Post
    
    //Public Post
    
}
