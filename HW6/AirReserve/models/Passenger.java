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
public class Passenger {
    
    private String _firstName;
    private String _middleName;
    private String _lastName;
    
    public String getFirstName() {
        return _firstName;
    }
    
    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    
    public String getMiddleName() {
        return _middleName;
    }
    
    public void setMiddleName(String middleName) {
        _middleName = middleName;
    }
    
    public String getLastName() {
        return _lastName;
    }
    
    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "first_name",
                "middle_name",
                "last_name"
            };
        return fieldNames;
    }
}
