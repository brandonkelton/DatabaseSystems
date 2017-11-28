/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.models;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author brand
 */
public class FlightInstance {
    
    private UUID _flightInstanceId;
    private String _airlineCode;
    private String _flightNumber;
    private Date _departureDate;
    private int _departureHour;
    private int _departureMinute;
    private Date _arrivalDate;
    private int _arrivalHour;
    private int _arrivalMinute;
    private Flight _flight;
    
    public UUID getFlightInstanceId() {
        return _flightInstanceId;
    }
    
    public void setFlightInstanceId(UUID flightInstanceId) {
        _flightInstanceId = flightInstanceId;
    }
    
    public String getAirlineCode() {
        return _airlineCode;
    }
    
    public void setAirlineCode(String airlineCode) {
        _airlineCode = airlineCode;
    }
    
    public String getFlightNumber() {
        return _flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        _flightNumber = flightNumber;
    }
    
    public Date getDepartureDate() {
        return _departureDate;
    }
    
    public void setDepartureDate(Date departureDate) {
        _departureDate = departureDate;
    }
    
    public int getDepartureHour() {
        return _departureHour;
    }
    
    public void setDepartureHour(int departureHour) {
        _departureHour = departureHour;
    }
    
    public int getDepartureMinute() {
        return _departureMinute;
    }
    
    public void setDepartureMinute(int departureMinute) {
        _departureMinute = departureMinute;
    }
    
    public Date getArrivalDate() {
        return _arrivalDate;
    }
    
    public void setArrivalDate(Date arrivalDate) {
        _arrivalDate = arrivalDate;
    }
    
    public int getArrivalHour() {
        return _arrivalHour;
    }
    
    public void setArrivalHour(int arrivalHour) {
        _arrivalHour = arrivalHour;
    }
    
    public int getArrivalMinute() {
        return _arrivalMinute;
    }
    
    public void setArrivalMinute(int arrivalMinute) {
        _arrivalMinute = arrivalMinute;
    }
    
    public Flight getFlight() {
        return _flight;
    }
    
    public void setFlight(Flight flight) {
        _flight = flight;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "flight_instance_id",
                "airline_code",
                "flight_number",
                "departure_date",
                "departure_hour",
                "departure_minute",
                "arrival_date",
                "arrival_hour",
                "arrival_minute"
            };
        return fieldNames;
    }
}
