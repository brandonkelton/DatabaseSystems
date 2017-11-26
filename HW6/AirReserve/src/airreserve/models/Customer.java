/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author brand
 */
public class Customer {
    
    private UUID _customerId;
    private String _firstName;
    private String _lastName;
    private String _street;
    private String _city;
    private String _stateProvince;
    private String _postalCode;
    private String _country;
    private List<Phone> _phones = new ArrayList<Phone>();
    private List<Email> _emails = new ArrayList<Email>();
    
    public UUID getCustomerId() {
        return _customerId;
    }
    
    public void setCustomerId(UUID customerId) {
        _customerId = customerId;
    }
   
    public String getFirstName() {
        return _firstName;
    }
    
    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    
    public String getLastName() {
        return _lastName;
    }
    
    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    
    public String getStreet() {
        return _street;
    }
    
    public void setStreet(String street) {
        _street = street;
    }
    
    public String getCity() {
        return _city;
    }
    
    public void setCity(String city) {
        _city = city;
    }
    
    public String getStateProvince() {
        return _stateProvince;
    }
    
    public void setStateProvince(String stateProvince) {
        _stateProvince = stateProvince;
    }
    
    public String getPostalCode() {
        return _postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }
    
    public String getCountry() {
        return _country;
    }
    
    public void setCountry(String country) {
        _country = country;
    }
    
    public List<Phone> getPhones() {
        return _phones;
    }
    
    public void setPhones(List<Phone> phones) {
        _phones = phones;
    }
    
    public List<Email> getEmails() {
        return _emails;
    }
    
    public void setEmails(List<Email> emails) {
        _emails = emails;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "customer_id",
                "first_name",
                "last_name",
                "street",
                "city",
                "state_province",
                "postal_code",
                "country"
            };
        return fieldNames;
    }
}
