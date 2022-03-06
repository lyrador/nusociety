/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

public enum AccessRightEnum
{
    MEMBER("MEMBER"),
    LEADER("LEADER");
    
    public final String name;
    
    private AccessRightEnum(String name) {
        this.name = name;
    }
}