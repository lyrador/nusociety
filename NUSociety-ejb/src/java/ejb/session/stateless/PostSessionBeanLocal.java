/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Post;
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
    
}
