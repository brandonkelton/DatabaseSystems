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
public class Airline {
    
    private String _airlineCode;
    private String _airlineName;
    
    public String getAirlineCode() {
        return _airlineCode;
    }
    
    public void setAirlineCode(String airlineCode) {
        _airlineCode = airlineCode;
    }
    
    public String getAirlineName() {
        return _airlineName;
    }
    
    public void setAirlineName(String airlineName) {
        _airlineName = airlineName;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "airline_code",
                "airline_name"
            };
        return fieldNames;
    }
}
