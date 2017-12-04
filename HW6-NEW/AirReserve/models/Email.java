/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.models;

/**
 *
 * @author brand
 */
public class Email {
    
    private String _email;
    private boolean _preferred;
    
    public String getEmail() {
        return _email;
    }
    
    public void setEmail(String email) {
        _email = email;
    }
    
    public boolean getPreferred() {
        return _preferred;
    }
    
    public void setPreferred(boolean preferred) {
        _preferred = preferred;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "preferred",
                "email"
            };
        return fieldNames;
    }
}
