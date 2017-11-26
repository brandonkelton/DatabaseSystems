/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.database;

import airreserve.helpers.StringHelpers;
import airreserve.models.Customer;
import airreserve.models.Email;
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
    
    public Customer getCustomerById(UUID id) {
        
        Customer customer = null;
        
        try {
            Connection conn = DriverManager.getConnection(_url, _props);
            Statement stmt = conn.createStatement();
            //List<String> conditions = getConditions(customer);
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
            //List<String> conditions = getConditions(customer);
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
            //List<String> conditions = getConditions(customer);
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
    
    public UUID createCustomer(Customer customer) {

        try {
            
            //Statement stmt = conn.createStatement();
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
