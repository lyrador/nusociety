/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author yeeda
 */
@Named(value = "enumValuesProvider")
@RequestScoped
public class EnumValuesProvider {

    /**
     * Creates a new instance of EnumValuesProvider
     */
    public EnumValuesProvider() {    
    }
    
    public AccessRightEnum[] getAccessRights() {
        return AccessRightEnum.values();
    }
    
}
