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
public class Flight {
    
    private String _flightNumber;
    private String _originCityCode;
    private String _destCityCode;
    private int _lengthHours;
    private int _lengthMinutes;
    
    public String getFlightNumber() {
        return _flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        _flightNumber = flightNumber;
    }
    
    public String getOriginCityCode() {
        return _originCityCode;
    }
    
    public void setOriginCityCode(String originCityCode) {
        _originCityCode = originCityCode;
    }
    
    public String getDestCityCode() {
        return _destCityCode;
    }
    
    public void setDestCityCode(String destCityCode) {
        _destCityCode = destCityCode;
    }
    
    public int getLengthHours() {
        return _lengthHours;
    }
    
    public void setLengthHours(int lengthHours) {
        _lengthHours = lengthHours;
    }
    
    public int getLengthMinutes() {
        return _lengthMinutes;
    }
    
    public void setLengthMinutes(int lengthMinutes) {
        _lengthMinutes = lengthMinutes;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "flight_number",
                "origin_city_code",
                "dest_city_code",
                "length_hours",
                "length_minutes"
            };
        return fieldNames;
    }
}
