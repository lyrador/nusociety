/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Comment;
import entity.Post;
import entity.Society;
import entity.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CommentNotFoundException;
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Stateless
public class PostSessionBean implements PostSessionBeanLocal {

    @EJB
    private CommentSessionBeanLocal commentSessionBean;

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPost(Post post) {
        Student student = em.find(Student.class, post.getStudent().getStudentId());
        Society society = em.find(Society.class, post.getSociety().getSocietyId());
        em.persist(post);
        
        //Add Post to student and society
        student.getPosts().add(post);
        society.getPosts().add(post);
        
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
    //Change the Long
    @Override
    public void updatePost(Long pId, Post newP) throws PostNotFoundException {
        Post p = retrievePostById(pId);
        
        //Update Body and Image
        p.setBodyContent(newP.getBodyContent());
        p.setPostIsPublic(newP.isPostIsPublic());
        p.setImage(newP.getImage()); 
        
    }
    
    @Override
    public void deletePost(Long postId) throws PostNotFoundException {
        Post p = retrievePostById(postId);
        
        if(p != null) {
            //Remove Society and Studnet
            p.getSociety().getPosts().remove(p);
            p.getStudent().getPosts().remove(p);
            
            List<Comment> commentList = new ArrayList<>();
            for(Comment c : p.getComments()) {
                commentList.add(c);
            }
            
            //Remove and Delete comments
            for(Comment c : commentList) {
                try {
                    commentSessionBean.deleteComment(c.getCommentId());
                } catch (CommentNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            p.getComments().clear();
            em.remove(p);
        } else {
            throw new PostNotFoundException("Post with ID: " + postId + "does not exists!");
        }
    }
    
    
    @Override
    public List<Comment> retrieveAllCommentsOfPost(Long postId) throws PostNotFoundException {
        Post p = retrievePostById(postId);
        
        if(p != null) {
            return p.getComments();
        } else {
            throw new PostNotFoundException("Post with ID: " + postId + "does not exists!");
        }
    }
    
    @Override
    public List<Post> retrieveAllPostsInDatabase() {
        Query query = em.createQuery("SELECT p FROM Post p");     
        return query.getResultList();
    }
    
    //Can delete afterwards
    @Override
    public Society retrieveSocietyById(Long societyId) {
        return em.find(Society.class, societyId);
    }
    
    //Retrieve all Private Post of societies
    
    //Retrieve all Public Post of societies
    
}
