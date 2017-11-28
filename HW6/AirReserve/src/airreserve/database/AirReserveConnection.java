/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.database;

import airreserve.helpers.StringHelpers;
import airreserve.models.Airline;
import airreserve.models.Booking;
import airreserve.models.City;
import airreserve.models.Customer;
import airreserve.models.Email;
import airreserve.models.Flight;
import airreserve.models.FlightInstance;
import airreserve.models.Passenger;
import airreserve.models.Phone;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brand
 */
public class AirReserveConnection {
    
    String _url;
    Properties _props;
    
    public AirReserveConnection() {
        configureConnection();
    }
    
    private void configureConnection() {
        _url = "jdbc:postgresql://localhost:5432/CSDB";
        _props = new Properties();
        _props.setProperty("user","postgres");
        _props.setProperty("password","ender1055");
    }
    
    public List<Customer> getSearchResults(String searchText) {
        
        List<Customer> customers = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            List<String> conditions = getSearchConditions(searchText);
            String query = 
                    "select " + String.join(", ", Customer.getFieldNames()) + " " +
                    "from airreserve.customer " +
                    "where " + String.join(" or ", conditions);
            ResultSet rs = stmt.executeQuery(query);
            customers = buildCustomerList(rs);
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return customers == null ? new ArrayList<>() : customers;
        
    }
    
    public List<City> getCities() {
        
        List<City> cities = new ArrayList<City>();
        
        try {
            
            String query = "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "order by city";
                     
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                City city = buildCity(rs);
                cities.add(city);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cities;
    }
    
    public List<FlightInstance> getFlightInstances(String originCity, String destCity) {
        
        List<FlightInstance> flights = new ArrayList<FlightInstance>();
        
        try {
            
            String query = 
                "select fi.flight_instance_id, fi.airline_code, fi.flight_number, fi.departure_date, fi.departure_hour, " + 
                "fi.departure_minute, fi.arrival_date, fi.arrival_hour, fi.arrival_minute " +
                "from airreserve.flight_instance fi " +
                "left join airreserve.flight f on fi.flight_number=f.flight_number " +
                "left join airreserve.city origin_city on f.origin_city_code=origin_city.city_code " +
                "left join airreserve.city dest_city on f.dest_city_code=dest_city.city_code " +
                "where origin_city.city=? and dest_city.city=? " +
                "order by fi.departure_date, fi.departure_hour, fi.departure_minute, " + 
                "fi.arrival_date, fi.arrival_hour, fi.arrival_minute";
                     
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, originCity);
            stmt.setString(2, destCity);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FlightInstance flight = buildFlightInstance(rs);
                flights.add(flight);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flights;
    }
    
    public FlightInstance getFlightInstanceByBookingId(UUID bookingId) {
        
        FlightInstance flightInstance = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + 
                        "fi.flight_instance_id, " +
                        "fi.airline_code, " +
                        "fi.flight_number, " +
                        "fi.departure_date, " +
                        "fi.departure_hour, " +
                        "fi.departure_minute, " +
                        "fi.arrival_date, " +
                        "fi.arrival_hour, " +
                        "fi.arrival_minute " +
                    "from airreserve.flight_instance fi " +
                    "join airreserve.booking_flight_instance bfi on fi.flight_instance_id=bfi.flight_instance_id " +
                    "where bfi.booking_id='" + bookingId.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            flightInstance = buildFlightInstance(rs);
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flightInstance == null ? new FlightInstance() : flightInstance;       
    }
    
    public List<Passenger> getPassengersByBookingId(UUID bookingId) {
        
        List<Passenger> passengers = new ArrayList<Passenger>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Passenger.getFieldNames()) + " " +
                    "from airreserve.booking_passenger " +
                    "where booking_id='" + bookingId.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
                        
            while (rs.next()) {
                Passenger passenger = buildPassenger(rs);
                passengers.add(passenger);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return passengers;
    }
    
    public Customer getCustomerById(UUID id) {
        
        Customer customer = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Customer.getFieldNames()) + " " +
                    "from airreserve.customer " +
                    "where customer_id='" + id.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            customer = buildCustomer(rs);
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return customer == null ? new Customer() : customer;
    }
    
    public List<Phone> getCustomerPhonesById(UUID id) {
        
        List<Phone> phones = new ArrayList<Phone>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Phone.getFieldNames()) + " " +
                    "from airreserve.customer_phone " +
                    "where customer_id='" + id.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            
            while (rs.next()) {
                Phone phone = buildPhone(rs);
                phones.add(phone);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return phones;
    }
    
    public List<Email> getCustomerEmailsById(UUID id) {
        
        List<Email> emails = new ArrayList<Email>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Email.getFieldNames()) + " " +
                    "from airreserve.customer_email " +
                    "where customer_id='" + id.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Email email = buildEmail(rs);
                emails.add(email);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return emails;
    }
    
    public List<Booking> getCustomerBookingsById(UUID id) {
        
        List<Booking> bookings = new ArrayList<Booking>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where customer_id='" + id.toString() + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Booking booking = buildBooking(rs);
                bookings.add(booking);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bookings;
    }
    
    public Booking getBookingById(UUID bookingId) {
        
        Booking booking = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where booking_id='" + bookingId.toString() + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            booking = buildBooking(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return booking == null ? new Booking() : booking;
    }
    
    public List<Booking> getBookingsByCustomerId(UUID customerId) {
        
        List<Booking> bookings = new ArrayList<Booking>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where customer_id='" + customerId.toString() + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Booking booking = buildBooking(rs);
                bookings.add(booking);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bookings;
    }
    
    public City getCityFromCityCode(String cityCode) {
        
        City city = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "where city_code='" + cityCode + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            city = buildCity(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return city == null ? new City() : city;
    }
    
    public City getCityFromCityName(String cityName) {
        
        City city = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "where city='" + cityName + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            city = buildCity(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return city == null ? new City() : city;
    }
    
    public Flight getFlightByFlightNumber(String flightNumber) {
        
        Flight flight = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Flight.getFieldNames()) + " " +
                    "from airreserve.flight " +
                    "where flight_number='" + flightNumber + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            flight = buildFlight(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flight == null ? new Flight() : flight;
    }
    
    public Airline getAirlineByAirlineName(String name) {
        
        Airline airline = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Airline.getFieldNames()) + " " +
                    "from airreserve.airline " +
                    "where airline_name='" + name + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            airline = buildAirline(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return airline == null ? new Airline() : airline;
    }
    
    public Airline getAirlineByAirlineCode(String code) {
        
        Airline airline = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", Airline.getFieldNames()) + " " +
                    "from airreserve.airline " +
                    "where airline_code='" + code + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            airline = buildAirline(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return airline == null ? new Airline() : airline;
    }
    
    public FlightInstance getFlightInstanceByAirlineCodeFlightNumber(String airlineCode, String flightNumber) {
    
        FlightInstance flight = null;

        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            
            String query = 
                    "select " + String.join(", ", FlightInstance.getFieldNames()) + " " +
                    "from airreserve.flight_instance " +
                    "where airline_code='" + airlineCode + "' and " +
                    "flight_number='" + flightNumber + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            flight = buildFlightInstance(rs);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flight == null ? new FlightInstance() : flight;
    }
    
    public UUID createCustomer(Customer customer) {

        try {
            
            String query = 
                    "insert into airreserve.customer " + 
                    "(first_name, last_name, street, city, state_province, postal_code, country) " +
                    "values (?,?,?,?,?,?,?);";
            String returnId[] = { "customer_id" };
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement stmt = conn.prepareStatement(query, returnId);
            
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getStreet());
            stmt.setString(4, customer.getCity());
            stmt.setString(5, customer.getStateProvince());
            stmt.setString(6, customer.getPostalCode());
            stmt.setString(7, customer.getCountry());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed. No rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId((UUID)generatedKeys.getObject(1));
                }
                else {
                    throw new SQLException("Created customer failed. No ID obtained.");
                }
            }
            finally {
                stmt.close();
                conn.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        savePhones(customer);
        saveEmails(customer);
        
        return customer.getCustomerId();
    }
    
    public UUID createBooking(Booking booking, UUID customerId) {
        
        try {
            
            String query = 
                    "insert into airreserve.booking " + 
                    "(customer_id, city_code, booking_date) " +
                    "values (?,?,?);";
            String returnId[] = { "booking_id" };
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement stmt = conn.prepareStatement(query, returnId);
            
            stmt.setObject(1, customerId);
            stmt.setString(2, booking.getCityCode());
            stmt.setTimestamp(3, booking.getBookingDate());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed. No rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId((UUID)generatedKeys.getObject(1));
                }
                else {
                    throw new SQLException("Creating booking failed. No ID obtained.");
                }
            }
            finally {
                stmt.close();
                conn.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveFlightInstance(booking);
        savePassengers(booking);
        
        return booking.getBookingId();        
    }
    
    public void deleteCustomer(Customer customer) {
        
        if (customer.getCustomerId() != null) {
            try {
                Connection conn = DriverManager.getConnection(_url, _props);
                Statement stmt = conn.createStatement();
                String query = 
                        "delete from airreserve.customer " + 
                        "where customer_id='" + customer.getCustomerId().toString() + "'";
                stmt.executeQuery(query);
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateCustomer(Customer customer) {
        
        try {
            
            String query = 
                    "update airreserve.customer set " + 
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "street = ?, " +
                    "city = ?, " +
                    "state_province = ?, " +
                    "postal_code = ?, " +
                    "country = ? " +
                    "where customer_id = ?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getStreet());
            stmt.setString(4, customer.getCity());
            stmt.setString(5, customer.getStateProvince());
            stmt.setString(6, customer.getPostalCode());
            stmt.setString(7, customer.getCountry());
            stmt.setObject(8, customer.getCustomerId());
            
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        savePhones(customer);
        saveEmails(customer);
    }
    
    public void updateBooking(Booking booking) {
        
        try {
            
            String query = 
                    "update airreserve.booking " +
                    "set " +
                    "city_code=?, " +
                    "booking_date=? " +
                    "where booking_id=?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, booking.getCityCode());
            stmt.setTimestamp(2, booking.getBookingDate());
                        
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveFlightInstance(booking);
        savePassengers(booking);
    }
    
    private void saveFlightInstance(Booking booking) {
        
        try {
            
            String deleteQuery = 
                    "delete from airreserve.booking_flight_instance " +
                    "where booking_id=?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            
            deleteStmt.setObject(1, booking.getBookingId());
            
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
                                    
            String query = 
                    "insert into airreserve.booking_flight_instance " +
                    "(booking_id, flight_instance_id) " +
                    "values (?,?)";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement insertStmt = conn.prepareStatement(query);
            
            insertStmt.setObject(1, booking.getBookingId());
            insertStmt.setObject(2, booking.getFlightInstance().getFlightInstanceId());

            insertStmt.executeUpdate();
            
            insertStmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    private void savePassengers(Booking booking) {
        
        try {
            
            String deleteQuery = 
                    "delete from airreserve.booking_passenger " +
                    "where booking_id=?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            
            deleteStmt.setObject(1, booking.getBookingId());
            
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
                                    
            String query = 
                    "insert into airreserve.booking_passenger " +
                    "(booking_id, first_name, middle_name, last_name) " +
                    "values (?,?,?,?)";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement insertStmt = conn.prepareStatement(query);
            
            for (Passenger p: booking.getPassengers()) {
                insertStmt.setObject(1, booking.getBookingId());
                insertStmt.setString(2, p.getFirstName());
                insertStmt.setString(3, p.getMiddleName());
                insertStmt.setString(4, p.getLastName());

                insertStmt.executeUpdate();
            }
            
            insertStmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void savePhones(Customer customer) {
        
        try {
            
            String deleteQuery = 
                    "delete from airreserve.customer_phone " +
                    "where customer_id=?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            
            deleteStmt.setObject(1, customer.getCustomerId());
            
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
                                    
            String query = 
                    "insert into airreserve.customer_phone " +
                    "(customer_id, phone_type, country_code, area_code, phone) " +
                    "values (?,?,?,?,?)";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement insertStmt = conn.prepareStatement(query);
            
            for (Phone p: customer.getPhones()) {
                insertStmt.setObject(1, customer.getCustomerId());
                insertStmt.setString(2, p.getPhoneType());
                insertStmt.setString(3, p.getCountryCode());
                insertStmt.setString(4, p.getAreaCode());
                insertStmt.setString(5, p.getPhone());

                insertStmt.executeUpdate();
            }
            
            insertStmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void saveEmails(Customer customer) {
        
        try {
            String deleteQuery = 
                    "delete from airreserve.customer_email " +
                    "where customer_id=?";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            
            deleteStmt.setObject(1, customer.getCustomerId());
            
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {    
            String query = 
                    "insert into airreserve.customer_email " +
                    "(customer_id, preferred, email) " +
                    "values (?,?,?)";
            
            Connection conn = DriverManager.getConnection(_url, _props);
            PreparedStatement insertStmt = conn.prepareStatement(query);
            
            for (Email e: customer.getEmails()) {
                insertStmt.setObject(1, customer.getCustomerId());
                insertStmt.setBoolean(2, e.getPreferred());
                insertStmt.setString(3, e.getEmail());

                insertStmt.executeUpdate();
            }
            
            insertStmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private List<String> getSearchConditions(String searchText) {
        
        List<String> conditions = new ArrayList<>();
        
        //conditions.add("customer_id = '" + searchText.replace("'", "''") + "'");
        conditions.add("first_name like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("last_name like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("street like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("city like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("state_province like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("postal_code like '%" + searchText.replace("'", "''") + "%'");
        conditions.add("country like '%" + searchText.replace("'", "''") + "%'");
                
        return conditions;
    }
    
    private List<Customer> buildCustomerList(ResultSet rs) {
        
        List<Customer> customers = new ArrayList<>();
        
        try {
            while (rs.next()) {
                customers.add(buildCustomer(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return customers;
    }
    
    private Customer buildCustomer(ResultSet rs) {
        
        Customer customer = new Customer();
        
        try {
            customer.setCustomerId(UUID.fromString(rs.getString("customer_id")));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setStreet(rs.getString("street"));
            customer.setCity(rs.getString("city"));
            customer.setStateProvince(rs.getString("state_province"));
            customer.setPostalCode(rs.getString("postal_code"));
            customer.setCountry(rs.getString("country"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return customer;
    }
    
    private Passenger buildPassenger(ResultSet rs) {
        
        Passenger passenger = new Passenger();
        
        try {
            passenger.setFirstName(rs.getString("first_name"));
            passenger.setMiddleName(rs.getString("middle_name"));
            passenger.setLastName(rs.getString("last_name"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return passenger;
    }
    
    private Booking buildBooking(ResultSet rs) {
        
        Booking booking = new Booking();
        
        try {
            booking.setBookingId(UUID.fromString(rs.getString("booking_id")));
            booking.setBookingDate(rs.getTimestamp("booking_date"));
            booking.setCityCode(rs.getString("city_code"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return booking;
    }
    
    private FlightInstance buildFlightInstance(ResultSet rs) {
        
        FlightInstance flight = new FlightInstance();
        
        try {
            flight.setFlightInstanceId(UUID.fromString(rs.getString("flight_instance_id")));
            flight.setAirlineCode(rs.getString("airline_code"));
            flight.setFlightNumber(rs.getString("flight_number"));
            flight.setArrivalDate(rs.getDate("arrival_date"));
            flight.setArrivalHour(rs.getInt("arrival_hour"));
            flight.setArrivalMinute(rs.getInt("arrival_minute"));
            flight.setDepartureDate(rs.getDate("departure_date"));
            flight.setDepartureHour(rs.getInt("departure_hour"));
            flight.setDepartureMinute(rs.getInt("departure_minute"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flight;
    }
    
    private Flight buildFlight(ResultSet rs) {
        
        Flight flight = new Flight();
        
        try {
            flight.setFlightNumber(rs.getString("flight_number"));
            flight.setOriginCityCode(rs.getString("origin_city_code"));
            flight.setDestCityCode(rs.getString("dest_city_code"));
            flight.setLengthHours(rs.getInt("length_hours"));
            flight.setLengthMinutes(rs.getInt("length_minutes"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flight;
    }
    
    private Airline buildAirline(ResultSet rs) {
        
        Airline airline = new Airline();
        
        try {
            airline.setAirlineCode(rs.getString("airline_code"));
            airline.setAirlineName(rs.getString("airline_name"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return airline;
    }
    
    private City buildCity(ResultSet rs) {
        
        City city = new City();
        
        try {
            city.setCityCode(rs.getString("city_code"));
            city.setCity(rs.getString("city"));
            city.setStateProvince(rs.getString("state_province"));
            city.setCountry(rs.getString("country"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return city;
    }
    
    private Phone buildPhone(ResultSet rs) {
        
        Phone phone = new Phone();
        
        try {
            phone.setPhoneType(rs.getString("phone_type"));
            phone.setCountryCode(rs.getString("country_code"));
            phone.setAreaCode(rs.getString("area_code"));
            phone.setPhone(rs.getString("phone"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return phone;
    }
    
    private Email buildEmail(ResultSet rs) {
        
        Email email = new Email();
        
        try {
            email.setPreferred(rs.getBoolean("preferred"));
            email.setEmail(rs.getString("email"));
        }
        catch (SQLException ex) {
            Logger.getLogger(AirReserveConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return email;
    }
}
