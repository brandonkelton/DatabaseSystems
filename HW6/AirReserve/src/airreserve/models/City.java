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
public class City {
    
    private String _cityCode;
    private String _city;
    private String _stateProvince;
    private String _country;
    
    public String getCityCode() {
        return _cityCode;
    }
    
    public void setCityCode(String cityCode) {
        _cityCode = cityCode;
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
    
    public String getCountry() {
        return _country;
    }
    
    public void setCountry(String country) {
        _country = country;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "city_code",
                "city",
                "state_province",
                "country"
            };
        return fieldNames;
    }
}
