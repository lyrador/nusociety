/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Comment;
import entity.Post;
import entity.Student;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CommentNotFoundException;
import util.exception.PostNotFoundException;
import util.exception.StudentNotFoundException;

/**
 *
 * @author 65912
 */
@Stateless
public class CommentSessionBean implements CommentSessionBeanLocal {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;
    

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    //(Requires Student and Post)
    //Create Comment
    @Override
    public Long createComment(Comment comment) {
        Post p = em.find(Post.class, comment.getPost().getPostId());
        Student s = em.find(Student.class, comment.getStudent().getStudentId());
        
        em.persist(comment);
        p.getComments().add(comment);
        s.getComments().add(comment);
        em.flush();
        
        return comment.getCommentId();
    }
    
    @Override
    public Comment retrieveCommentById(Long commentId) throws CommentNotFoundException {
        Comment c = em.find(Comment.class, commentId);
        
        if(c != null) {
            return c;
        } else {
            throw new CommentNotFoundException("Comment with Id: " + commentId + " cannot be found!");
        }
    }
    
    //Update Comment
    @Override
    public void updateComment(Comment newC) throws CommentNotFoundException {
        Comment c = retrieveCommentById(newC.getCommentId());
        
        //Only update content, Date shouldn't be updated
        c.setContent(newC.getContent());
    }
    
    //Delete Comment
    @Override
    public void deleteComment(Long commentId) throws CommentNotFoundException {
        Comment c = retrieveCommentById(commentId);
        
        //Disassociate Student and Post
        c.getPost().getComments().remove(c);
        c.getStudent().getComments().remove(c);
        em.remove(c);
    }
    
    //View List of Comment of a post
    @Override
    public List<Comment> viewAllCommentsOfPost(Long postId) throws PostNotFoundException {
        Post p = postSessionBean.retrievePostById(postId);
        
        if(p != null) {
            return p.getComments();
        } else {
            throw new PostNotFoundException("Error, Post with ID:" + postId + " not found!");
        }
    }
    
    //View List of Comment of a Student
    //Exception handling
    @Override
    public List<Comment> viewAllCommentsOfStudent(Long studentId) throws StudentNotFoundException {
        
        
        try {
            Student s = studentSessionBean.retrieveStudentByStudentId(studentId);
            return s.getComments();
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException("Student with Id: " + studentId + " cannot be found!");
        }
    }
    
    @Override
    public List<Comment> viewAllCommentsInDatabase() {
        Query query = em.createQuery("SELECT c FROM Comment c");     
        return query.getResultList();
    }
    
}
