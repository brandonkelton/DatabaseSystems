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
public class Phone {
    
    private String _countryCode;
    private String _areaCode;
    private String _phone;
    private String _phoneType;
    
    public String getCountryCode() {
        return _countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        _countryCode = countryCode;
    }
    
    public String getAreaCode() {
        return _areaCode;
    }
    
    public void setAreaCode(String areaCode) {
        _areaCode = areaCode;
    }
    
    public String getPhone() {
        return _phone;
    }
    
    public void setPhone(String phone) {
        _phone = phone;
    }
    
    public String getPhoneType() {
        return _phoneType;
    }
    
    public void setPhoneType(String phoneType) {
        _phoneType = phoneType;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "phone_type",
                "country_code",
                "area_code",
                "phone"
            };
        return fieldNames;
    }
}
