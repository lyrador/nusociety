/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author ajayan
 */
public class EventAlreadyExistsException extends Exception{

    public EventAlreadyExistsException() {
    }

    public EventAlreadyExistsException(String string) {
        super(string);
    }
 
}
