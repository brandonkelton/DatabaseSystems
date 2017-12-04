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
import java.io.IOException;
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
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author brand
 */
public class AirReserveConnection {
    
    public static Logger ARLogger = Logger.getLogger("AirReserveConnectionLog");
    public static boolean LogInitialized = false;
    
    String _url;
    Properties _props;
    FileHandler fh;
    
    public AirReserveConnection() {
        configureConnection();
        configureLog();
    }
    
    private void configureLog() {
        
        if (!LogInitialized) {
            try {
                fh = new FileHandler("C:\\Users\\brand\\AirReserveConnection.log");
                ARLogger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();  
                fh.setFormatter(formatter); 
                ARLogger.info("AirReserveConnection Initialized.");
                LogInitialized = true;
            } catch (SecurityException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
    }
    
    private void configureConnection() {
        _url = "jdbc:postgresql://localhost:5432/CSDB";
        _props = new Properties();
        _props.setProperty("user","postgres");
        _props.setProperty("password","ender1055");
    }
    
    public <T extends ResultSet, R> DbResult<R> getResults(Function<T, R> func, String query) {
        
        DbResult<R> result = new DbResult<R>();
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            T rs = (T)stmt.executeQuery(query);
            result.setResult(func.apply(rs));
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        return result;
    }
    
    private <T extends ResultSet, R> R build(Function<T, R> func, ResultSet rs) {

        R result = null;
        
        try {
            if (rs.next()) {
                result = func.apply((T)rs);
            }
        }
        catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
        }        
        
        return result;
    }
    
    private <T extends ResultSet, R> List<R> buildList(Function<T, R> func, ResultSet rs) {
        
        List<R> resultList = new ArrayList<R>();
        
        try {
            while (rs.next()) {
                resultList.add(func.apply((T)rs));
            }
        }
        catch (Exception ex) {
            ARLogger.severe(ex.getMessage());
        }
        
        return resultList;
    }
        
    public DbResult<List<Customer>> getSearchResults(String searchText) {
        
        List<String> conditions = getSearchConditions(searchText);
        
        String query = 
                    "select " + String.join(", ", Customer.getFieldNames()) + " " +
                    "from airreserve.customer " +
                    "where " + String.join(" or ", conditions);
        
        DbResult<List<Customer>> result = 
                getResults((x) -> buildList((y) -> buildCustomer(y), x), query);
        
        return result;
    }
    
    public DbResult<List<City>> getCities() {
        
        String query = "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "order by city";
        
        DbResult<List<City>> result = 
                getResults((x) -> buildList((y) -> buildCity(y), x), query);
        
        return result;
    }
    
    public DbResult<List<FlightInstance>> getFlightInstances(String originCity, String destCity) {
                
        DbResult<List<FlightInstance>> result = new DbResult<List<FlightInstance>>();
        
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
            List<FlightInstance> flights = new ArrayList<FlightInstance>();
            
            while (rs.next()) {
                FlightInstance flight = buildFlightInstance(rs);
                flights.add(flight);
            }
            
            result.setResult(flights);
            
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        return result;
    }
    
    public DbResult<FlightInstance> getFlightInstanceByBookingId(UUID bookingId) {
        
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
        
        DbResult<FlightInstance> result = 
                getResults((x) -> build((y) -> buildFlightInstance(y), x), query);
        
        return result;     
    }
    
    public DbResult<List<Passenger>> getPassengersByBookingId(UUID bookingId) {
        
        String query = 
                    "select " + String.join(", ", Passenger.getFieldNames()) + " " +
                    "from airreserve.booking_passenger " +
                    "where booking_id='" + bookingId.toString() + "'";
        
        DbResult<List<Passenger>> result = 
                getResults((x) -> buildList((y) -> buildPassenger(y), x), query);
        
        return result;
    }
    
    public DbResult<Customer> getCustomerById(UUID id) {
        
        String query = 
                    "select " + String.join(", ", Customer.getFieldNames()) + " " +
                    "from airreserve.customer " +
                    "where customer_id='" + id.toString() + "'";
        
        DbResult<Customer> result =
                getResults((x) -> build((y) -> buildCustomer(y), x), query);
        
        return result;
    }
    
    public DbResult<List<Phone>> getCustomerPhonesById(UUID id) {
        
        String query = 
                    "select " + String.join(", ", Phone.getFieldNames()) + " " +
                    "from airreserve.customer_phone " +
                    "where customer_id='" + id.toString() + "'";
        
        DbResult<List<Phone>> result = 
                getResults((x) -> buildList((y) -> buildPhone(y), x), query);
        
        return result;
    }
    
    public DbResult<List<Email>> getCustomerEmailsById(UUID id) {
        
        String query = 
                    "select " + String.join(", ", Email.getFieldNames()) + " " +
                    "from airreserve.customer_email " +
                    "where customer_id='" + id.toString() + "'";
        
        DbResult<List<Email>> result =
                getResults((x) -> buildList((y) -> buildEmail(y), x), query);
        
        return result;
    }
    
    public DbResult<List<Booking>> getCustomerBookingsById(UUID id) {
        
        String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where customer_id='" + id.toString() + "'";
        
        DbResult<List<Booking>> result =
                getResults((x) -> buildList((y) -> buildBooking(y), x), query);
        
        return result;
    }
    
    public DbResult<Booking> getBookingById(UUID bookingId) {
        
        String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where booking_id='" + bookingId.toString() + "'";
        
        DbResult<Booking> result =
                getResults((x) -> build((y) -> buildBooking(y), x), query);
        
        return result;
    }
    
    public DbResult<List<Booking>> getBookingsByCustomerId(UUID customerId) {
        
        String query = 
                    "select " + String.join(", ", Booking.getFieldNames()) + " " +
                    "from airreserve.booking " +
                    "where customer_id='" + customerId.toString() + "'";
                   
        DbResult<List<Booking>> result =
                getResults((x) -> buildList((y) -> buildBooking(y), x), query);
        
        return result;
    }
    
    public DbResult<City> getCityFromCityCode(String cityCode) {
        
        String query = 
                    "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "where city_code='" + cityCode + "'";
        
        DbResult<City> result = 
                getResults((x) -> build((y) -> buildCity(y), x), query);
        
        return result;
    }
    
    public DbResult<City> getCityFromCityName(String cityName) {
        
        String query = 
                    "select " + String.join(", ", City.getFieldNames()) + " " +
                    "from airreserve.city " +
                    "where city='" + cityName + "'";
        
        DbResult<City> result =
                getResults((x) -> build((y) -> buildCity(y), x), query);
        
        return result;
    }
    
    public DbResult<Flight> getFlightByFlightNumber(String flightNumber) {
        
        String query = 
                    "select " + String.join(", ", Flight.getFieldNames()) + " " +
                    "from airreserve.flight " +
                    "where flight_number='" + flightNumber + "'";
        
        DbResult<Flight> result = 
                getResults((x) -> build((y) -> buildFlight(y), x), query);
        
        return result;
    }
    
    public DbResult<Airline> getAirlineByAirlineName(String name) {
        
        String query = 
                    "select " + String.join(", ", Airline.getFieldNames()) + " " +
                    "from airreserve.airline " +
                    "where airline_name='" + name + "'";
        
        DbResult<Airline> result =
                getResults((x) -> build((y) -> buildAirline(y), x), query);
        
        return result;
    }
    
    public DbResult<Airline> getAirlineByAirlineCode(String code) {
        
        String query = 
                    "select " + String.join(", ", Airline.getFieldNames()) + " " +
                    "from airreserve.airline " +
                    "where airline_code='" + code + "'";
        
        DbResult<Airline> result =
                getResults((x) -> build((y) -> buildAirline(y), x), query);
        
        return result;
    }
    
    public DbResult<FlightInstance> getFlightInstanceByAirlineCodeFlightNumber(String airlineCode, String flightNumber) {
    
        String query = 
                    "select " + String.join(", ", FlightInstance.getFieldNames()) + " " +
                    "from airreserve.flight_instance " +
                    "where airline_code='" + airlineCode + "' and " +
                    "flight_number='" + flightNumber + "'";
        
        DbResult<FlightInstance> result =
                getResults((x) -> build((y) -> buildFlightInstance(y), x), query);
        
        return result;
    }
    
    public DbResult<Customer> createCustomer(Customer customer) {

        DbResult<Customer> result = new DbResult<Customer>();
        
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
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                customer.setCustomerId((UUID)generatedKeys.getObject(1));
            }
            else {
                throw new SQLException("Created customer failed. No ID obtained.");
            }
            
            stmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            
            ARLogger.severe(ex.getMessage());
            result.setResult(customer);
            result.setIsError(true);
            result.setMessage(ex.getMessage());
            
            return result;
        }
        
        IDbResult phoneResult = savePhones(customer);
        result.setMessage(phoneResult.getMessage());
        IDbResult emailResult = saveEmails(customer);
        result.setMessage(emailResult.getMessage());
        
        result.setResult(customer);
        
        return result;
    }
    
    public IDbResult<Booking> createBooking(Booking booking, UUID customerId) {
        
        DbResult<Booking> result = new DbResult<Booking>();
        
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
                    saveFlightInstance(booking);
                    savePassengers(booking);
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
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        result.setResult(booking);
        
        return result;        
    }
    
    public IDbResult<Customer> updateCustomer(Customer customer) {
        
        IDbResult<Customer> result = new DbResult<Customer>();
        
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
            
            IDbResult phoneResult = savePhones(customer);
            if (phoneResult.getIsError()) {
                result.setIsError(true);
                result.setMessage(phoneResult.getMessage());
            }
            
            IDbResult emailResult = saveEmails(customer);
            if (emailResult.getIsError()) {
                result.setIsError(true);
                result.setMessage(emailResult.getMessage());
            }
            
        } catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        result.setResult(customer);
        
        return result;
    }
    
    public IDbResult<Booking> updateBooking(Booking booking) {
        
        IDbResult<Booking> result = new DbResult<Booking>();
        
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
            stmt.setObject(3, booking.getBookingId());
                        
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            saveFlightInstance(booking);
            savePassengers(booking);
        
            //result.setUpdateComplete(true);
        }
        catch (Exception ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        result.setResult(booking);
        
        return result;
    }
    
    private IDbResult saveFlightInstance(Booking booking) {
        
        IDbResult result = new DbResult();
        
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
            ARLogger.severe(ex.getMessage());
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
            
            //result.setUpdateComplete(true);
            
        } catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
          
        return result;
    }
    
    private IDbResult savePassengers(Booking booking) {
        
        IDbResult result = new DbResult();
        
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
            ARLogger.severe(ex.getMessage());
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
            
            //result.setUpdateComplete(true);
            
        } catch (SQLException ex) {
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        return result;
    }
    
    private IDbResult savePhones(Customer customer) {
        
        IDbResult result = new DbResult();
        
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
            ARLogger.severe(ex.getMessage());
            //Don't need to pass delete errors back up to user in this case
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
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        return result;
    }
    
    private IDbResult saveEmails(Customer customer) {
        
        IDbResult result = new DbResult();
        
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
            ARLogger.severe(ex.getMessage());
            //Don't need to pass errors back to user in this case
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
            ARLogger.severe(ex.getMessage());
            result.setIsError(true);
            result.setMessage(ex.getMessage());
        }
        
        return result;
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
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
            ARLogger.severe(ex.getMessage());
        }
        
        return email;
    }
}
