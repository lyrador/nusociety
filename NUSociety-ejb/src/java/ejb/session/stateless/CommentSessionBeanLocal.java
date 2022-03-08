/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Comment;
import java.util.List;
import javax.ejb.Local;
import util.exception.CommentNotFoundException;
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Local
public interface CommentSessionBeanLocal {

    public List<Comment> viewAllCommentsOfPost(Long postId) throws PostNotFoundException;

    public List<Comment> viewAllCommentsOfStudent(Long studentId);

    public Long createComment(Comment comment);

    public Comment retrieveCommentById(Long commentId) throws CommentNotFoundException;

    public void updateComment(Comment newC) throws CommentNotFoundException;

    public void deleteComment(Long commentId) throws CommentNotFoundException;
    
}
