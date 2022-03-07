/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 65912
 */
@Stateless
public class CommentSessionBean implements CommentSessionBeanLocal {

    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    //(Requires Student and Post)
    //Create Comment
    
    //Delete Comment
    
    //View List of Comment of a post
    
    //Update Comment?

    
}
