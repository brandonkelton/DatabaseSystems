/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.models;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author brand
 */
public class Booking {
    
    private UUID _bookingId;
    private String _cityCode;
    private Timestamp _bookingDate;
    private FlightInstance _flightInstance;
    private List<Passenger> _passengers;
        
    public UUID getBookingId() {
        return _bookingId;
    }
    
    public void setBookingId(UUID bookingId) {
        _bookingId = bookingId;
    }
    
    public String getCityCode() {
        return _cityCode;
    }
    
    public void setCityCode(String cityCode) {
        _cityCode = cityCode;
    }
    
    public Timestamp getBookingDate() {
        return _bookingDate;
    }
    
    public void setBookingDate(Timestamp bookingDate) {
        _bookingDate = bookingDate;
    }
    
    public FlightInstance getFlightInstance() {
        return _flightInstance;
    }
    
    public void setFlightInstance(FlightInstance flightInstance) {
        _flightInstance = flightInstance;
    }
    
    public List<Passenger> getPassengers() {
        return _passengers;
    }
    
    public void setPassengers(List<Passenger> passengers) {
        _passengers = passengers;
    }
    
    public static String[] getFieldNames() {
        String fieldNames[] = 
            {
                "booking_id",
                "city_code",
                "booking_date"
            };
        return fieldNames;
    }
}
