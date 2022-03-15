/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Comment;
import entity.Post;
import entity.Society;
import java.util.List;
import javax.ejb.Local;
import util.exception.PostNotFoundException;

/**
 *
 * @author 65912
 */
@Local
public interface PostSessionBeanLocal {

    public Long createNewPost(Post post);

    public Post retrievePostById(Long postId) throws PostNotFoundException;

    public void updatePost(Long pId, Post newP) throws PostNotFoundException;

    public void deletePost(Long postId) throws PostNotFoundException;

    public List<Comment> retrieveAllCommentsOfPost(Long postId) throws PostNotFoundException;

    public List<Post> retrieveAllPostsInDatabase();

    public Society retrieveSocietyById(Long societyId);
    
}
