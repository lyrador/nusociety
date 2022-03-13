/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author 65912
 */
public class CommentNotFoundException extends Exception{

    public CommentNotFoundException() {
    }

    public CommentNotFoundException(String string) {
        super(string);
    }
    
}
