/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve;

import airreserve.database.AirReserveConnection;
import static airreserve.database.AirReserveConnection.ARLogger;
import static airreserve.database.AirReserveConnection.LogInitialized;
import airreserve.database.DbResult;
import airreserve.database.IDbResult;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author brandon kelton
 */
public class AirReserveUI extends javax.swing.JFrame {

    public static Logger ARLogger = Logger.getLogger("AirReserveBookingLog");
    public static boolean LogInitialized = false;
    private boolean _isNewBooking = false;
    FileHandler fh;
    
    public AirReserveUI() {
        initComponents();
        configureLog();
        setupButtonsAndLabels();
        setEditableFields(false);
        configureListeners();
        loadCities();
        clearBooking();
    }
    
    private void configureLog() {
        
        if (!LogInitialized) {
            try {
                fh = new FileHandler("C:\\Users\\brand\\AirReserveBooking.log");
                ARLogger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();  
                fh.setFormatter(formatter); 
                ARLogger.info("AirReserveUI Initialized.");
                LogInitialized = true;
            } catch (SecurityException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
    }
    
    private void configureListeners() {
        tableCustomerLookup.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int row = tableCustomerLookup.getSelectedRow();
                loadCustomerData(row);
            }
        });
        tableBookings.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int row = tableBookings.getSelectedRow();
                loadBooking(row);
            }
        });
        comboBoxOriginCity.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                updateAvailableFlights();
            }
        });
        comboBoxDestinationCity.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                updateAvailableFlights();
            }
        });
    }
    
    private void setupButtonsAndLabels() {
        buttonSave.setEnabled(false);
        buttonClear.setEnabled(false);
    }
    
    private void setEditableFields(boolean editable) {
        
        textFirstName.setEnabled(editable);
        textLastName.setEnabled(editable);
        textStreet.setEnabled(editable);
        textCity.setEnabled(editable);
        textStateProvince.setEnabled(editable);
        textPostalCode.setEnabled(editable);
        textCountry.setEnabled(editable);
        
        tablePhone.setEnabled(editable);
        buttonNewPhone.setEnabled(editable);
        tableEmail.setEnabled(editable);
        buttonNewEmail.setEnabled(editable);
        tableFlights.setEnabled(editable);
        tablePassengers.setEnabled(editable);
        comboBoxOriginCity.setEnabled(editable);
        comboBoxDestinationCity.setEnabled(editable);
        buttonNewBooking.setEnabled(editable);
        buttonAddPassenger.setEnabled(editable);
        buttonRemovePassenger.setEnabled(editable);
    }
    
    private void loadCities() {
        
        AirReserveConnection conn = new AirReserveConnection();
        IDbResult<List<City>> result = conn.getCities();
        
        if (ShowErrorMessage(result, "Error Loading Cities")) {
            return;
        }
                
        result.getResult().forEach(c -> {
            comboBoxBookingCity.addItem(c.getCity());
            comboBoxOriginCity.addItem(c.getCity());
            comboBoxDestinationCity.addItem(c.getCity());
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textSearch = new javax.swing.JTextField();
        scrollPaneCustomerLookup = new javax.swing.JScrollPane();
        tableCustomerLookup = new javax.swing.JTable();
        buttonSearch = new javax.swing.JButton();
        buttonNewCustomer = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        labelAirReserve = new javax.swing.JLabel();
        tabbedPaneMain = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        textStreet = new javax.swing.JTextField();
        labelStreet = new javax.swing.JLabel();
        textFirstName = new javax.swing.JTextField();
        labelFirstName = new javax.swing.JLabel();
        labelLastName = new javax.swing.JLabel();
        textLastName = new javax.swing.JTextField();
        textCity = new javax.swing.JTextField();
        labelCity = new javax.swing.JLabel();
        labelStateProvince = new javax.swing.JLabel();
        textStateProvince = new javax.swing.JTextField();
        textPostalCode = new javax.swing.JTextField();
        labelPostalCode = new javax.swing.JLabel();
        textCountry = new javax.swing.JTextField();
        labelCountry = new javax.swing.JLabel();
        textCustomerId = new javax.swing.JTextField();
        labelCustomerId = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePhone = new javax.swing.JTable();
        buttonNewPhone = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableEmail = new javax.swing.JTable();
        buttonNewEmail = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableBookings = new javax.swing.JTable();
        buttonNewBooking = new javax.swing.JButton();
        textBookingId = new javax.swing.JTextField();
        labelBookingId = new javax.swing.JLabel();
        comboBoxOriginCity = new javax.swing.JComboBox<>();
        labelOriginCity = new javax.swing.JLabel();
        comboBoxDestinationCity = new javax.swing.JComboBox<>();
        labelDestinationCity = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableFlights = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablePassengers = new javax.swing.JTable();
        buttonAddPassenger = new javax.swing.JButton();
        buttonRemovePassenger = new javax.swing.JButton();
        buttonEditCustomer = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        comboBoxBookingCity = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchActionPerformed(evt);
            }
        });
        textSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textSearchKeyPressed(evt);
            }
        });

        tableCustomerLookup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer ID", "Last Name", "First Name", "Country", "State/Province", "City", "Street"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCustomerLookup.setColumnSelectionAllowed(true);
        tableCustomerLookup.getTableHeader().setReorderingAllowed(false);
        tableCustomerLookup.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tableCustomerLookupPropertyChange(evt);
            }
        });
        scrollPaneCustomerLookup.setViewportView(tableCustomerLookup);
        tableCustomerLookup.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        buttonSearch.setText("Search");
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed(evt);
            }
        });

        buttonNewCustomer.setText("New Customer");
        buttonNewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewCustomerActionPerformed(evt);
            }
        });

        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        labelAirReserve.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelAirReserve.setText("AirReserve");

        textStreet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textStreetActionPerformed(evt);
            }
        });

        labelStreet.setText("Street");

        textFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFirstNameActionPerformed(evt);
            }
        });

        labelFirstName.setText("First Name");

        labelLastName.setText("Last Name");

        textLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textLastNameActionPerformed(evt);
            }
        });

        textCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCityActionPerformed(evt);
            }
        });

        labelCity.setText("City");

        labelStateProvince.setText("State/Province");

        textStateProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textStateProvinceActionPerformed(evt);
            }
        });

        textPostalCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPostalCodeActionPerformed(evt);
            }
        });

        labelPostalCode.setText("Postal Code");

        textCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCountryActionPerformed(evt);
            }
        });

        labelCountry.setText("Country");

        textCustomerId.setEditable(false);
        textCustomerId.setEnabled(false);

        labelCustomerId.setText("Customer ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelStreet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textStreet)
                            .addComponent(textFirstName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textLastName)
                            .addComponent(labelLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                            .addComponent(textCity)
                            .addComponent(labelCity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textStateProvince)
                            .addComponent(labelStateProvince, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPostalCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 69, Short.MAX_VALUE))
                            .addComponent(textCountry)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCustomerId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFirstName)
                            .addComponent(labelLastName))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelStreet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelCity)
                                    .addComponent(labelCountry))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelStateProvince)
                            .addComponent(labelPostalCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textStateProvince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(176, Short.MAX_VALUE))
        );

        tabbedPaneMain.addTab("General", jPanel1);

        tablePhone.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Phone Type", "Country Code", "Area Code", "Phone"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablePhone.setColumnSelectionAllowed(true);
        tablePhone.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablePhone);
        tablePhone.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tablePhone.getColumnModel().getColumnCount() > 0) {
            tablePhone.getColumnModel().getColumn(0).setResizable(false);
            tablePhone.getColumnModel().getColumn(3).setResizable(false);
        }

        buttonNewPhone.setText("New");
        buttonNewPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewPhoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonNewPhone)
                .addContainerGap(260, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonNewPhone)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPaneMain.addTab("Phone", jPanel2);

        tableEmail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Preferred", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableEmail.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableEmail);
        tableEmail.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tableEmail.getColumnModel().getColumnCount() > 0) {
            tableEmail.getColumnModel().getColumn(0).setResizable(false);
            tableEmail.getColumnModel().getColumn(1).setResizable(false);
        }

        buttonNewEmail.setText("New");
        buttonNewEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonNewEmail)
                .addContainerGap(260, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(buttonNewEmail)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPaneMain.addTab("Email", jPanel3);

        tableBookings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "City", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableBookings.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tableBookings);
        tableBookings.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tableBookings.getColumnModel().getColumnCount() > 0) {
            tableBookings.getColumnModel().getColumn(0).setResizable(false);
            tableBookings.getColumnModel().getColumn(1).setResizable(false);
            tableBookings.getColumnModel().getColumn(2).setResizable(false);
        }

        buttonNewBooking.setText("New Booking");
        buttonNewBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewBookingActionPerformed(evt);
            }
        });

        textBookingId.setEnabled(false);

        labelBookingId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelBookingId.setText("Booking ID:");

        labelOriginCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelOriginCity.setText("Origin City:");

        labelDestinationCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelDestinationCity.setText("Destination City:");

        tableFlights.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Airline", "Flight", "Depart", "Arrive"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableFlights.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tableFlights);
        tableFlights.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tableFlights.getColumnModel().getColumnCount() > 0) {
            tableFlights.getColumnModel().getColumn(0).setResizable(false);
            tableFlights.getColumnModel().getColumn(1).setResizable(false);
            tableFlights.getColumnModel().getColumn(2).setResizable(false);
            tableFlights.getColumnModel().getColumn(3).setResizable(false);
        }

        tablePassengers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Middle Name", "Last Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablePassengers.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tablePassengers);
        tablePassengers.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tablePassengers.getColumnModel().getColumnCount() > 0) {
            tablePassengers.getColumnModel().getColumn(0).setResizable(false);
            tablePassengers.getColumnModel().getColumn(1).setResizable(false);
            tablePassengers.getColumnModel().getColumn(2).setResizable(false);
        }

        buttonAddPassenger.setText("Add Passenger");
        buttonAddPassenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddPassengerActionPerformed(evt);
            }
        });

        buttonRemovePassenger.setText("Remove Passenger");
        buttonRemovePassenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemovePassengerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(buttonNewBooking)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 47, Short.MAX_VALUE)
                                .addComponent(labelBookingId)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textBookingId, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelOriginCity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelDestinationCity, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(comboBoxDestinationCity, 0, 343, Short.MAX_VALUE)
                                    .addComponent(comboBoxOriginCity, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonRemovePassenger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonAddPassenger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonNewBooking)
                            .addComponent(textBookingId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelBookingId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(comboBoxDestinationCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelDestinationCity)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboBoxOriginCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelOriginCity)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(buttonAddPassenger)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonRemovePassenger)))))
                .addContainerGap())
        );

        tabbedPaneMain.addTab("Reservations", jPanel4);

        buttonEditCustomer.setText("Edit");
        buttonEditCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditCustomerActionPerformed(evt);
            }
        });

        buttonClear.setText("Cancel");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        jLabel1.setText("Booking City:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneCustomerLookup)
                    .addComponent(tabbedPaneMain, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelAirReserve, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxBookingCity, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(textSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonSearch))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonNewCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonEditCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSearch)
                    .addComponent(textSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxBookingCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAirReserve, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(27, 27, 27)
                .addComponent(scrollPaneCustomerLookup, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonNewCustomer)
                    .addComponent(buttonSave)
                    .addComponent(buttonEditCustomer)
                    .addComponent(buttonClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
        fillResultTable(getSearchResults(textSearch.getText()));
    }//GEN-LAST:event_buttonSearchActionPerformed

    private void tableCustomerLookupPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tableCustomerLookupPropertyChange
        
    }//GEN-LAST:event_tableCustomerLookupPropertyChange

    private void textSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textSearchActionPerformed
        
    }//GEN-LAST:event_textSearchActionPerformed

    private void updateAvailableFlights() {
        
        String originCity = comboBoxOriginCity.getSelectedItem() != null ? comboBoxOriginCity.getSelectedItem().toString() : "";
        String destCity = comboBoxDestinationCity.getSelectedItem() != null ? comboBoxDestinationCity.getSelectedItem().toString() : "";
        
        if ("".equals(originCity) || "".equals(destCity)) return;
        
        AirReserveConnection conn = new AirReserveConnection();
        IDbResult<List<FlightInstance>> result = conn.getFlightInstances(originCity, destCity);
        
        if (ShowErrorMessage(result, "Error Loading Available Flights")) {
            return;
        }
            
        DefaultTableModel tableModel = 
            (DefaultTableModel) tableFlights.getModel();
        
        //Reset table rows to empty
        tableModel.setRowCount(0);
        
        for (FlightInstance f : result.getResult()) {
            
            IDbResult<Airline> rowResult = conn.getAirlineByAirlineCode(f.getAirlineCode());
            
            if (ShowErrorMessage(rowResult, "Error Loading Flights")) {
                return;
            }
            
            tableModel.addRow(
                new Object[] 
                {
                    rowResult.getResult().getAirlineName(),
                    f.getFlightNumber(),
                    f.getDepartureDate() + " " + 
                            (f.getDepartureHour() < 10 ? "0" : "") + f.getDepartureHour() + 
                            ":" + (f.getDepartureMinute() < 10 ? "0" : "") + f.getDepartureMinute(),
                    f.getArrivalDate() + " " + 
                            (f.getArrivalHour() < 10 ? "0" : "") + f.getArrivalHour() + 
                            ":" + (f.getArrivalMinute() < 10 ? "0" : "") + f.getArrivalMinute()
                });
        }
    }
    
    private Customer buildCustomerModel() {
        
        Customer customer = new Customer();
        
        if (!StringHelpers.isNullOrEmpty(textCustomerId.getText())) {
            customer.setCustomerId(UUID.fromString(textCustomerId.getText()));
        }
        customer.setFirstName(textFirstName.getText());
        customer.setLastName(textLastName.getText());
        customer.setStreet(textStreet.getText());
        customer.setCity(textCity.getText());
        customer.setStateProvince(textStateProvince.getText());
        customer.setPostalCode(textPostalCode.getText());
        customer.setCountry(textCountry.getText());
        customer.setPhones(getCustomerPhones());
        customer.setEmails(getCustomerEmails());
        
        return customer;
    }
    
    private Booking buildBookingModel() {
        
        Booking booking = new Booking();
                
        AirReserveConnection conn = new AirReserveConnection();
        IDbResult<City> result = conn.getCityFromCityName(comboBoxBookingCity.getSelectedItem().toString());
        
        
        
        if (!StringHelpers.isNullOrEmpty(textBookingId.getText())) {
            booking.setBookingId(UUID.fromString(textBookingId.getText()));
        }
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setCityCode(result.getResult().getCityCode());
        booking.setFlightInstance(getSelectedFlightInstance());
        booking.setPassengers(buildPassengerList());
        
        return booking;
    }
    
    private FlightInstance getSelectedFlightInstance() {
        
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        int rowIndex = tableFlights.getSelectedRow();
        
        if (rowIndex < 0) {
            return  null;
        }
        
        AirReserveConnection conn = new AirReserveConnection();
        
        IDbResult<Airline> airlineResult = conn.getAirlineByAirlineName(model.getValueAt(rowIndex, 0).toString());
        
        if (ShowErrorMessage(airlineResult, "Error Loading Airline")) {
            return null;
        }
        
        String flightNumber = model.getValueAt(rowIndex, 1).toString();
        
        IDbResult<FlightInstance> flightResult = conn.getFlightInstanceByAirlineCodeFlightNumber(airlineResult.getResult().getAirlineCode(), flightNumber);
        
        if (ShowErrorMessage(flightResult, "Error Loading Flight")) {
            return null;
        }
        
        return flightResult.getResult();
    }
    
    private List<Passenger> buildPassengerList() {
        
        List<Passenger> passengers = new ArrayList<Passenger>();
        
        DefaultTableModel model = (DefaultTableModel) tablePassengers.getModel();
        
        for (int i=0;i<model.getRowCount();i++) {
            Passenger passenger = new Passenger();
            passenger.setFirstName(model.getValueAt(i, 0).toString());
            passenger.setMiddleName(model.getValueAt(i, 1).toString());
            passenger.setLastName(model.getValueAt(i, 2).toString());
            passengers.add(passenger);
        }
        
        return passengers;
    }
    
    private List<Phone> getCustomerPhones() {
        
        List<Phone> phones = new ArrayList<Phone>();
        DefaultTableModel model = (DefaultTableModel) tablePhone.getModel();
        
        for (int i=0;i<model.getRowCount();i++) {
            
            Phone phone = new Phone();
            phone.setPhoneType(model.getValueAt(i, 0).toString());
            phone.setCountryCode(model.getValueAt(i, 1).toString());
            phone.setAreaCode(model.getValueAt(i, 2).toString());
            phone.setPhone(model.getValueAt(i, 3).toString());
            phones.add(phone);
            
        }
        
        return phones;
    }
    
    private List<Email> getCustomerEmails() {
        
        List<Email> emails = new ArrayList<Email>();
        DefaultTableModel model = (DefaultTableModel) tableEmail.getModel();
        
        for (int i=0;i<model.getRowCount();i++) {
            
            Email email = new Email();
            email.setPreferred((boolean)model.getValueAt(i, 0));
            email.setEmail(model.getValueAt(i, 1).toString());
            emails.add(email);
            
        }
        
        return emails;
    }
    
    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
                       
        IDbResult<Customer> customerResult = saveCustomer();
        IDbResult<Booking> bookingResult = saveBooking();
        
        if (!customerResult.getIsError() && !bookingResult.getIsError()) {
            
            LogBooking(customerResult.getResult(), bookingResult.getResult());
            
            exitEditMode();
            return;
        }
        
        IDbResult errorResult = customerResult.getIsError() ? customerResult : bookingResult;
        ShowErrorMessage(errorResult, "Error Saving Data");
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void textStreetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textStreetActionPerformed
        
    }//GEN-LAST:event_textStreetActionPerformed

    private void textCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCityActionPerformed
        
    }//GEN-LAST:event_textCityActionPerformed

    private void textStateProvinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textStateProvinceActionPerformed
        
    }//GEN-LAST:event_textStateProvinceActionPerformed

    private void textPostalCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPostalCodeActionPerformed
        
    }//GEN-LAST:event_textPostalCodeActionPerformed

    private void textCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCountryActionPerformed
        
    }//GEN-LAST:event_textCountryActionPerformed

    private void buttonNewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewCustomerActionPerformed
        clearCustomerData();
        enterEditMode();
    }//GEN-LAST:event_buttonNewCustomerActionPerformed

    private void textFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFirstNameActionPerformed
        
    }//GEN-LAST:event_textFirstNameActionPerformed

    private void textLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textLastNameActionPerformed
        
    }//GEN-LAST:event_textLastNameActionPerformed

    private void textSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textSearchKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 13) {
            fillResultTable(getSearchResults(textSearch.getText()));
        }
    }//GEN-LAST:event_textSearchKeyPressed

    private void buttonEditCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditCustomerActionPerformed
        if (!StringHelpers.isNullOrEmpty(textCustomerId.getText())) {
            enterEditMode();
        }
    }//GEN-LAST:event_buttonEditCustomerActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        exitEditMode();
    }//GEN-LAST:event_buttonClearActionPerformed

    private void buttonNewPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewPhoneActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablePhone.getModel();
        model.addRow(new Object[]{"","","",""});
    }//GEN-LAST:event_buttonNewPhoneActionPerformed

    private void buttonNewEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewEmailActionPerformed
        DefaultTableModel model = (DefaultTableModel) tableEmail.getModel();
        model.addRow(new Object[]{false,""});
    }//GEN-LAST:event_buttonNewEmailActionPerformed

    private void buttonAddPassengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddPassengerActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablePassengers.getModel();
        model.addRow(new Object[]{"","",""});
    }//GEN-LAST:event_buttonAddPassengerActionPerformed

    private void buttonRemovePassengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemovePassengerActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablePassengers.getModel();
        model.removeRow(tablePassengers.getSelectedRow());
    }//GEN-LAST:event_buttonRemovePassengerActionPerformed

    private void buttonNewBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewBookingActionPerformed
        
        loadBooking(-1);
        textBookingId.setText("");
        comboBoxOriginCity.setSelectedItem(null);
        comboBoxDestinationCity.setSelectedItem(null);
        
        DefaultTableModel flightModel = (DefaultTableModel) tableFlights.getModel();
        flightModel.setRowCount(0);
        
        DefaultTableModel passengerModel = (DefaultTableModel) tablePassengers.getModel();
        passengerModel.setRowCount(0);
    }//GEN-LAST:event_buttonNewBookingActionPerformed
    
    private void enterEditMode() {
        setEditableFields(true);
        buttonSave.setEnabled(true);
        buttonClear.setEnabled(true);
        textSearch.setEnabled(false);
        buttonSearch.setEnabled(false);
        tableCustomerLookup.setEnabled(false);
    }
    
    private void exitEditMode() {
        setEditableFields(false);
        buttonSave.setEnabled(false);
        buttonClear.setEnabled(false);
        textSearch.setEnabled(true);
        buttonSearch.setEnabled(true);
        tableCustomerLookup.setEnabled(true);
    }
    
    private List<Customer> getSearchResults(String searchText) {
        
        AirReserveConnection conn = new AirReserveConnection();
        IDbResult<List<Customer>> dbResult = conn.getSearchResults(searchText);
        
        return dbResult.getResult();
    }
    
    private void fillResultTable(List<Customer> customers) {
        
        DefaultTableModel tableModel = 
                (DefaultTableModel) tableCustomerLookup.getModel();
        
        //Reset table rows to empty
        tableModel.setRowCount(0);
        
        customers.forEach((_item) -> {
            tableModel.addRow(
                    new Object[] 
                    {
                        _item.getCustomerId(),
                        _item.getLastName(),
                        _item.getFirstName(),
                        _item.getCountry(),
                        _item.getStateProvince(),
                        _item.getCity(),
                        _item.getStreet()
                    });
        });
    }
    
    private boolean ShowErrorMessage(IDbResult result, String title) {
        
        if (result == null) {
            JOptionPane.showMessageDialog(null, "There was a problem loading the data.", title, JOptionPane.ERROR_MESSAGE);
            return true;
        }
        
        if (result.getIsError()) {
            JOptionPane.showMessageDialog(null, result.getMessage(), title, JOptionPane.ERROR_MESSAGE);
            return true;
        }
        
        return false;
    }
    
    private void loadCustomerData(int rowIndex) {
        
        DefaultTableModel tableModel = (DefaultTableModel) tableCustomerLookup.getModel();
        UUID customerId = (UUID)tableModel.getValueAt(rowIndex, 0);
        
        AirReserveConnection conn = new AirReserveConnection();
        
        IDbResult<Customer> customerResult = conn.getCustomerById(customerId);
        if (ShowErrorMessage(customerResult, "Error Loading Customer")) {
            return;
        }
        
        IDbResult<List<Phone>> phoneResult = conn.getCustomerPhonesById(customerId);
        if (ShowErrorMessage(phoneResult, "Error Loading Phone Data")) {
            return;
        }
        customerResult.getResult().setPhones(phoneResult.getResult());
        
        IDbResult<List<Email>> emailResult = conn.getCustomerEmailsById(customerId);
        if (ShowErrorMessage(emailResult, "Error Loading Email Data")) {
            return;
        }
        customerResult.getResult().setEmails(emailResult.getResult());
        
        IDbResult<List<Booking>> bookingResult = conn.getCustomerBookingsById(customerId);
        if (ShowErrorMessage(bookingResult, "Error Loading Bookings")) {
            return;
        }
        customerResult.getResult().setBookings(bookingResult.getResult());
        
        loadCustomerData(customerResult.getResult());
    }
    
    private void clearBooking() {
        
        textBookingId.setText("");
        comboBoxOriginCity.setSelectedItem(null);
        comboBoxDestinationCity.setSelectedItem(null);
        
        DefaultTableModel flightModel = (DefaultTableModel) tableFlights.getModel();
        flightModel.setRowCount(0);
        
        DefaultTableModel passengerModel = (DefaultTableModel) tablePassengers.getModel();
        passengerModel.setRowCount(0);
    }
    
    private void loadBooking(int rowIndex) {
        
        if (rowIndex < 0) {
            clearBooking();
            _isNewBooking = true;
            return;
        }
        
        _isNewBooking = false;
        
        DefaultTableModel tableModel = (DefaultTableModel) tableBookings.getModel();
        UUID bookingId = (UUID)tableModel.getValueAt(rowIndex, 0);
        
        AirReserveConnection conn = new AirReserveConnection();
        
        IDbResult<Booking> bookingResult = conn.getBookingById(bookingId);
        if (ShowErrorMessage(bookingResult, "Error Loading Booking")) {
            return;
        }
        
        IDbResult<FlightInstance> flightInstanceResult = conn.getFlightInstanceByBookingId(bookingId);
        if (ShowErrorMessage(flightInstanceResult, "Error Loading Flight Data")) {
            return;
        }
        bookingResult.getResult().setFlightInstance(flightInstanceResult.getResult());
        
        FlightInstance fi = bookingResult.getResult().getFlightInstance();
        if (fi != null) {
            
            String flightNumber = fi.getFlightNumber();
            
            IDbResult<Flight> flightResult = conn.getFlightByFlightNumber(flightNumber);
            if (ShowErrorMessage(flightResult, "Error Loading Flight")) {
                return;
            }
            bookingResult.getResult().getFlightInstance().setFlight(flightResult.getResult());
        }
        
        IDbResult<List<Passenger>> passengerResult = conn.getPassengersByBookingId(bookingId);
        if (ShowErrorMessage(passengerResult, "Error Loading Passengers")) {
            return;
        }
        bookingResult.getResult().setPassengers(passengerResult.getResult());
        
        loadBooking(bookingResult.getResult());
    }
    
    private void loadBooking(Booking booking) {
        
        textBookingId.setText(booking.getBookingId().toString());
        
        AirReserveConnection conn = new AirReserveConnection();
        
        IDbResult<City> originCityResult = null;
        IDbResult<City> destCityResult = null;
        
        FlightInstance fi = booking.getFlightInstance();
        if (fi != null) {
            Flight f = fi.getFlight();
            if (f != null) {
                originCityResult = conn.getCityFromCityCode(f.getOriginCityCode());
                destCityResult = conn.getCityFromCityCode(f.getDestCityCode());
            }
        }
        
        IDbResult<City> errorCityResult = (originCityResult == null ? destCityResult == null ? null : destCityResult : originCityResult);
        
        if (ShowErrorMessage(errorCityResult, "Error Loading City")) {
            return;
        }
                
        if (originCityResult != null)
            comboBoxOriginCity.setSelectedItem(originCityResult.getResult().getCity());
        
        if (destCityResult != null)
            comboBoxDestinationCity.setSelectedItem(destCityResult.getResult().getCity());
        
        updateAvailableFlights();
        
        DefaultTableModel flightTableModel = (DefaultTableModel) tableFlights.getModel();
        
        IDbResult<Airline> airlineResult = conn.getAirlineByAirlineCode(booking.getFlightInstance().getAirlineCode());
        if (ShowErrorMessage(airlineResult, "Error Loading Airline Data")) {
            return;
        }
        
        int rowIndex = getRowByValues(
                flightTableModel, 
                0, 
                airlineResult.getResult().getAirlineName(), 
                1, 
                booking.getFlightInstance().getFlightNumber());
                
        if (rowIndex > -1) {
            tableFlights.setRowSelectionInterval(rowIndex, rowIndex);
        }
        
        DefaultTableModel passengerTableModel = 
                (DefaultTableModel) tablePassengers.getModel();
        
        //Reset table rows to empty
        passengerTableModel.setRowCount(0);
        
        booking.getPassengers().forEach((p) -> {
            passengerTableModel.addRow(
                    new Object[] 
                    {
                        p.getFirstName(),
                        p.getMiddleName(),
                        p.getLastName()
                    });
        });
    }
    
    private int getRowByValues(TableModel model, int colIndex1, Object value1, int colIndex2, Object value2) {
        
        for (int i=0;i<model.getRowCount();i++) {
            if (model.getValueAt(i, colIndex1).equals(value1) &&
                model.getValueAt(i, colIndex2).equals(value2)) {
                return i;
            }
        }
        
        return -1;
    }
    
    private IDbResult saveCustomer() {
        
        IDbResult<Customer> customerResult;
        
        Customer customer = buildCustomerModel();
        
        IDbResult validCustomerResult = isCustomerValid(customer);
        if (validCustomerResult.getIsError()) {
            return validCustomerResult;
        }
                
        AirReserveConnection conn = new AirReserveConnection();
        
        if (!StringHelpers.isNullOrEmpty(textCustomerId.getText())) {
            customerResult = conn.updateCustomer(customer);
        }
        else {
            customerResult = conn.createCustomer(customer);
            textCustomerId.setText(customerResult.getResult().getCustomerId().toString());
        }
        
        customerResult.setResult(customer);
        
        return customerResult;
    }
    
    private IDbResult isCustomerValid(Customer customer) {
        
        IDbResult result = new DbResult();
        
        if (StringHelpers.isNullOrEmpty(customer.getFirstName()) ||
                StringHelpers.isNullOrEmpty(customer.getLastName()) ||
                StringHelpers.isNullOrEmpty(customer.getStreet()) ||
                StringHelpers.isNullOrEmpty(customer.getCity()) ||
                StringHelpers.isNullOrEmpty(customer.getStateProvince()) ||
                StringHelpers.isNullOrEmpty(customer.getPostalCode()) ||
                StringHelpers.isNullOrEmpty(customer.getCountry())) {
            
            result.setIsError(true);
            result.setMessage("Customers must have the following:\r\n\r\n" +
                    "First Name\r\nLastName\r\nStreet\r\nCity\r\nState\\Province\r\nPostal Code\r\nCountry");
            
            return result;
        }
        
        return result;
    }
    
    private IDbResult arePassengersValid() {
        
        IDbResult result = new DbResult();
        DefaultTableModel passengerModel = (DefaultTableModel)tablePassengers.getModel();
        
        if (tablePassengers.getRowCount() == 0) {
            result.setIsError(true);
            result.setMessage("At least one passenger is required.");
            return result;
        }
        
        for (int i=0;i<tablePassengers.getRowCount();i++) {
            
            String firstName = passengerModel.getValueAt(i, 0).toString();
            String lastName = passengerModel.getValueAt(i, 2).toString();
            
            if ("".equals(firstName) || "".equals(lastName)) {
                result.setIsError(true);
                result.setMessage("Passengers require both a first name and a last name.");
            }
        }
        
        return result;
    }
    
    private IDbResult<Booking> isBookingValid() {
        
        IDbResult<Booking> result = new DbResult<Booking>();
        
        DefaultTableModel flightModel = (DefaultTableModel) tableFlights.getModel();
        DefaultTableModel passengerModel = (DefaultTableModel) tablePassengers.getModel();
        
        IDbResult passengerResult = arePassengersValid();
        
        if (tableFlights.getSelectedRow() < 0 || 
                passengerResult.getIsError() ||
                comboBoxOriginCity.getSelectedItem() == null &&
                comboBoxDestinationCity.getSelectedItem() == null) {
            
            result.setIsError(true);
            result.setMessage("A flight must be selected, passengers must be without error, and origin and destination cities must be selected.");
        }
        
        return result;
    }
 
    private IDbResult<Booking> saveBooking() {
                        
        //If not a "new booking" AND not an existing booking, no booking was created, so skip saving it
        if (!_isNewBooking && StringHelpers.isNullOrEmpty(textBookingId.getText())) 
            return new DbResult<Booking>();
                
        //Determine if booking is valid
        IDbResult<Booking> bookingValidResult = isBookingValid();
        if (bookingValidResult.getIsError()) {
            return bookingValidResult;
        }
        
        AirReserveConnection conn = new AirReserveConnection();
        
        //Load customer for reference in booking log; if it doesn't load, error
        IDbResult<Customer> customerResult = conn.getCustomerById(UUID.fromString(textCustomerId.getText()));
        if (ShowErrorMessage(customerResult, "Error Loading Customer")) {
            IDbResult<Booking> bookingFromCustomerResult = new DbResult<Booking>();
            bookingFromCustomerResult.setIsError(true);
            bookingFromCustomerResult.setMessage(customerResult.getMessage());
            return bookingFromCustomerResult;
        }
        
        UUID bookingId;
        Booking booking = buildBookingModel();
        
        IDbResult<Booking> bookingResult;
        
        if (StringHelpers.isNullOrEmpty(textBookingId.getText())) {
            bookingResult = conn.createBooking(booking, UUID.fromString(textCustomerId.getText()));
            if (ShowErrorMessage(bookingResult, "Error Saving Booking")) {
                return bookingResult;
            }
            bookingId = bookingResult.getResult().getBookingId();
            textBookingId.setText(bookingId.toString());
        }
        else {
            bookingResult = conn.updateBooking(booking);
            if (ShowErrorMessage(bookingResult, "Error Updating Booking")) {
                return bookingResult;
            }
        }
        
        updateBookingList();
        selectBooking(bookingResult.getResult().getBookingId());
        
        return bookingResult;
    }
    
    private void LogBooking(Customer customer, Booking booking) {
        
        String messageTemplate = 
                "%s booked %s flight number %s for %s at %s.\r\n" +
                "The passengers are:\r\n%s\r\n" +
                "Booking was created on %s in the city of %s.";
        
        List<String> passengers = new ArrayList<String>();
        for (Passenger passenger : booking.getPassengers()) {
            passengers.add(
                    passenger.getFirstName() + " " + 
                    (StringHelpers.isNullOrEmpty(
                            passenger.getMiddleName()) ? "" : 
                            passenger.getMiddleName() + " ") + 
                    passenger.getLastName());
        }
        
        String message = String.format(messageTemplate,
                customer.getFirstName() + " " + customer.getLastName(),
                booking.getFlightInstance().getAirlineCode(),
                booking.getFlightInstance().getFlightNumber(),
                booking.getFlightInstance().getDepartureDate(),
                getFormattedTime(booking.getFlightInstance().getDepartureHour(), booking.getFlightInstance().getDepartureMinute()),
                String.join("\r\n", passengers), 
                booking.getBookingDate(), 
                booking.getCityCode());
        
        ARLogger.info(message);
        
        JOptionPane.showMessageDialog(null, message, "Flight Booked!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getFormattedTime(int hour, int minute) {
        
        String time = "";
        
        if (hour < 10) {
            time += "0";
        }
        
        time += hour + ":";
        
        if (minute < 10) {
            time += "0";
        }
        
        time += minute;
        
        return time;
    }
    
    private void selectBooking(UUID bookingId) {
        
        DefaultTableModel tableModel = 
                (DefaultTableModel) tableBookings.getModel();
        
        for (int i=0;i<tableModel.getRowCount();i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(bookingId.toString())) {
                tableBookings.setRowSelectionInterval(i, i);
            }
        }
    }
    
    private void updateBookingList() {
        
        AirReserveConnection conn = new AirReserveConnection();
        IDbResult<List<Booking>> bookingsResult = conn.getBookingsByCustomerId(UUID.fromString(textCustomerId.getText()));
        if (ShowErrorMessage(bookingsResult, "Error Loading Existing Bookings")) {
            return;
        }
        
        DefaultTableModel tableModel = (DefaultTableModel) tableBookings.getModel();
        
        tableModel.setRowCount(0);
        
        bookingsResult.getResult().forEach((b) -> {
            tableModel.addRow(
                    new Object[] 
                    {
                        b.getBookingId(),
                        b.getCityCode(),
                        b.getBookingDate()
                    });
        });
    }
    
    private void loadCustomerData(Customer customer) {
        //I would normally use binding to assign the data to the fields,
        //but I don't know this IDE or Java very well. Used to Visual Studio and C#.
        //Also, I don't think you intended for me to go this far with the project  ;)
        
        textCustomerId.setText(customer.getCustomerId().toString());
        textFirstName.setText(customer.getFirstName());
        textLastName.setText(customer.getLastName());
        textStreet.setText(customer.getStreet());
        textCity.setText(customer.getCity());
        textStateProvince.setText(customer.getStateProvince());
        textPostalCode.setText(customer.getPostalCode());
        textCountry.setText(customer.getCountry());
        
        DefaultTableModel phoneTableModel = 
                (DefaultTableModel) tablePhone.getModel();
        
        //Reset table rows to empty
        phoneTableModel.setRowCount(0);
        
        customer.getPhones().forEach(p -> {
            phoneTableModel.addRow(
                    new Object[] 
                    {
                        p.getPhoneType(),
                        p.getCountryCode(),
                        p.getAreaCode(),
                        p.getPhone()
                    });
        });
        
        DefaultTableModel emailTableModel = 
                (DefaultTableModel) tableEmail.getModel();
        
        //Reset table rows to empty
        emailTableModel.setRowCount(0);
        
        customer.getEmails().forEach(e -> {
            emailTableModel.addRow(
                    new Object[] 
                    {
                        e.getPreferred(),
                        e.getEmail()
                    });
        });
        
        DefaultTableModel bookingTableModel = 
                (DefaultTableModel) tableBookings.getModel();
        
        //Reset table rows to empty
        bookingTableModel.setRowCount(0);
        
        customer.getBookings().forEach(b -> {
            bookingTableModel.addRow(
                    new Object[] 
                    {
                        b.getBookingId(),
                        b.getCityCode(),
                        b.getBookingDate()
                    });
        });
        
        if (customer.getBookings().toArray().length > 0) {
            tableBookings.setRowSelectionInterval(0,0);
            loadBooking(0);
            _isNewBooking = false;
        }
        else {
            _isNewBooking = true;
        }
    }
    
    private void clearCustomerData() {
        
        textCustomerId.setText("");
        textFirstName.setText("");
        textLastName.setText("");
        textStreet.setText("");
        textCity.setText("");
        textStateProvince.setText("");
        textPostalCode.setText("");
        textCountry.setText("");
        textBookingId.setText("");
        comboBoxOriginCity.setSelectedItem(null);
        comboBoxDestinationCity.setSelectedItem(null);
                
        DefaultTableModel phoneModel = (DefaultTableModel) tablePhone.getModel();
        phoneModel.setRowCount(0);
        
        DefaultTableModel emailModel = (DefaultTableModel) tableEmail.getModel();
        emailModel.setRowCount(0);
        
        DefaultTableModel bookingModel = (DefaultTableModel) tableBookings.getModel();
        bookingModel.setRowCount(0);
        
        DefaultTableModel flightModel = (DefaultTableModel) tableFlights.getModel();
        flightModel.setRowCount(0);
        
        DefaultTableModel passengerModel = (DefaultTableModel) tablePassengers.getModel();
        passengerModel.setRowCount(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AirReserveUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AirReserveUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AirReserveUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AirReserveUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirReserveUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddPassenger;
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonEditCustomer;
    private javax.swing.JButton buttonNewBooking;
    private javax.swing.JButton buttonNewCustomer;
    private javax.swing.JButton buttonNewEmail;
    private javax.swing.JButton buttonNewPhone;
    private javax.swing.JButton buttonRemovePassenger;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JComboBox<String> comboBoxBookingCity;
    private javax.swing.JComboBox<String> comboBoxDestinationCity;
    private javax.swing.JComboBox<String> comboBoxOriginCity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelAirReserve;
    private javax.swing.JLabel labelBookingId;
    private javax.swing.JLabel labelCity;
    private javax.swing.JLabel labelCountry;
    private javax.swing.JLabel labelCustomerId;
    private javax.swing.JLabel labelDestinationCity;
    private javax.swing.JLabel labelFirstName;
    private javax.swing.JLabel labelLastName;
    private javax.swing.JLabel labelOriginCity;
    private javax.swing.JLabel labelPostalCode;
    private javax.swing.JLabel labelStateProvince;
    private javax.swing.JLabel labelStreet;
    private javax.swing.JScrollPane scrollPaneCustomerLookup;
    private javax.swing.JTabbedPane tabbedPaneMain;
    private javax.swing.JTable tableBookings;
    private javax.swing.JTable tableCustomerLookup;
    private javax.swing.JTable tableEmail;
    private javax.swing.JTable tableFlights;
    private javax.swing.JTable tablePassengers;
    private javax.swing.JTable tablePhone;
    private javax.swing.JTextField textBookingId;
    private javax.swing.JTextField textCity;
    private javax.swing.JTextField textCountry;
    private javax.swing.JTextField textCustomerId;
    private javax.swing.JTextField textFirstName;
    private javax.swing.JTextField textLastName;
    private javax.swing.JTextField textPostalCode;
    private javax.swing.JTextField textSearch;
    private javax.swing.JTextField textStateProvince;
    private javax.swing.JTextField textStreet;
    // End of variables declaration//GEN-END:variables
}
