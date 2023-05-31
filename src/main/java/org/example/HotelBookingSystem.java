package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class HotelBookingSystem extends JFrame {

    Connection conn;
    JTable table;
    Box contents = new Box(BoxLayout.Y_AXIS);
    JTextField empId, empName, department, salary, dtJoin, emailId;
    // Column name
    private final static String[] COLUMNS = {"ID", "GuestName", "ContactNo", "Room", "Check_in", "Check_out", "EMailID"};

    public HotelBookingSystem() {
        conn = getConnection();
        table = new JTable();
        List<PeopleInfo> rawData = getRawDataFromDB(conn);
        DefaultTableModel model = getModel(rawData);
        drawTable(model);
        drawComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    conn.close();
                    System.out.println("Connection to SQLite has been closed.");
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
                System.exit(0);
            }
        });
    }

    private void refreshData() {
        List<PeopleInfo> rawData = getRawDataFromDB(conn);
        DefaultTableModel model = getModel(rawData);
        table.setModel(model);
    }

    private void drawComponents() {
        Box contentsButton = new Box(BoxLayout.X_AXIS);

        JButton newModalButton = new JButton("Create");
        newModalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rooms = {"Simple", "Deluxe", "SuperDeluxe"};
                JTextField guestName = new JTextField();
                JTextField contact = new JTextField();
                JComboBox room = new JComboBox<>(rooms);
                JTextField checkIn = new JTextField();
                JTextField checkOut = new JTextField();
                JTextField emailId = new JTextField();
                final JComponent[] inputs = new JComponent[]{
                        new JLabel(COLUMNS[1]),
                        guestName,
                        new JLabel(COLUMNS[2]),
                        contact,
                        new JLabel(COLUMNS[3]),
                        room,
                        new JLabel(COLUMNS[4]),
                        checkIn,
                        new JLabel(COLUMNS[5]),
                        checkOut,
                        new JLabel(COLUMNS[6]),
                        emailId,
                };

                int result = JOptionPane.showConfirmDialog(null, inputs, "Do you want add new guest?", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String sql = "INSERT INTO HotelTable values(?,?,?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        //ps.setString(1, empId.getText());
                        ps.setString(2, guestName.getText());
                        ps.setString(3, contact.getText());
                        ps.setString(4, (String) (room.getSelectedItem()));
                        ps.setString(5, checkIn.getText());
                        ps.setString(6, checkOut.getText());
                        ps.setString(7, emailId.getText());
                        //This method is when update/insert/delete any row
                        ps.executeUpdate();
                        System.out.println("Item created successfully");

                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    System.out.println("User canceled / closed the dialog, result = " + result);
                }
                refreshData();
            }
        });
        contentsButton.add(newModalButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() > -1) {
                    Integer guestIdValue = (Integer) (table.getValueAt(table.getSelectedRow(), 0));
                    String guestNameValue = (String) (table.getValueAt(table.getSelectedRow(), 1));
                    String guestNoValue = (String) (table.getValueAt(table.getSelectedRow(), 2));
                    String roomValue = (String) (table.getValueAt(table.getSelectedRow(), 3));
                    String checkInValue = (String) (table.getValueAt(table.getSelectedRow(), 4));
                    String checkOutValue = (String) (table.getValueAt(table.getSelectedRow(), 5));
                    String emailIdValue = (String) (table.getValueAt(table.getSelectedRow(), 6));

                    String[] rooms = {"Simple", "Deluxe", "SuperDeluxe"};
                    JTextField guestName = new JTextField();
                    JTextField contact = new JTextField();
                    JComboBox room = new JComboBox<>(rooms);
                    JTextField checkIn = new JTextField();
                    JTextField checkOut = new JTextField();
                    JTextField emailId = new JTextField();
                    final JComponent[] inputs = new JComponent[]{
                            new JLabel("guestName"),
                            guestName,
                            new JLabel("contactNo"),
                            contact,
                            new JLabel("room"),
                            room,
                            new JLabel("checkIn"),
                            checkIn,
                            new JLabel("checkOut"),
                            checkOut,
                            new JLabel("emailId"),
                            emailId,
                    };
                    guestName.setText(guestNameValue);
                    contact.setText(guestNoValue);
                    room.setSelectedItem(roomValue);
                    checkIn.setText(checkInValue);
                    checkOut.setText(checkOutValue);
                    emailId.setText(emailIdValue);

                    int result = JOptionPane.showConfirmDialog(null, inputs, "Update dialog", JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            String sql = "UPDATE HotelTable SET GuestName = ?, Contact = ?, Room = ?, Check_in = ?, Check_out = ?, EMailID = ? WHERE Id = ?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, guestName.getText());
                            ps.setString(2, contact.getText());
                            ps.setString(3, (String) (room.getSelectedItem()));
                            ps.setString(4, checkIn.getText());
                            ps.setString(5, checkOut.getText());
                            ps.setString(6, emailId.getText());
                            ps.setInt(7, guestIdValue);

                            //This method is when update/insert/delete any row
                            ps.executeUpdate();
                            System.out.println("Item updated successfully");

                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("User canceled / closed the dialog, result = " + result);
                    }
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(HotelBookingSystem.this, "Item not selected", "Selected item", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        contentsButton.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() > -1) {
                    Integer guestId = (Integer) (table.getModel().getValueAt(table.getSelectedRow(), 0));
                    System.out.println(guestId);
                    int result = JOptionPane.showConfirmDialog(null, "Do you want delete guest with id: " + guestId + " ?", "Delete guest", JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            String sql = "DELETE FROM HotelTable WHERE Id=?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setInt(1, guestId);
                            ps.executeUpdate();
                            System.out.println("Item deleted successfully");

                            refreshData();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("User canceled / closed the dialog, result = " + result);
                    }
                } else {
                    JOptionPane.showMessageDialog(HotelBookingSystem.this, "Item not selected", "Selected item", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        contentsButton.add(deleteButton);

        //contents.add(contentsLabel);
        contents.add(contentsButton);
        getContentPane().add(contents);
    }

    private void drawTable(DefaultTableModel model) {
        // Create Table
        table = new JTable(model);
        //Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table));
    }

    private DefaultTableModel getModel(List<PeopleInfo> rawData) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(COLUMNS);
        for (PeopleInfo people : rawData) {
            Object[] item = new Object[7];
            item[0] = people.getId();
            item[1] = people.getGuestName();
            item[2] = people.getContactNo();
            item[3] = people.getRoom();
            item[4] = people.getCheckIn();
            item[5] = people.getCheckOut();
            item[6] = people.getEmailID();
            model.addRow(item);
        }
        return model;
    }

    private List<PeopleInfo> getRawDataFromDB(Connection conn) {
        List<PeopleInfo> rawData = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();

            //DriverManager is checking the accessibility to DB
            String sql = "SELECT * FROM HotelTable ORDER BY Id DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PeopleInfo people = new PeopleInfo();

                people.setGuestId(rs.getInt("Id"));
                people.setGuestName(rs.getString("GuestName"));
                people.setContactNo(rs.getString("Contact"));
                people.setRoom(rs.getString("Room"));
                people.setCheckIn(rs.getString("Check_in"));
                people.setCheckOut(rs.getString("Check_out"));
                people.setEmailID(rs.getString("EMailID"));

                rawData.add(people);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rawData;
    }

    private Connection getConnection() {
        Connection conn = null; //same
        try {
            // db parameters
            String url = "jdbc:sqlite:.\\src\\main\\java\\org\\example\\sqlite.db"; //same
            // create a connection to the database
            conn = DriverManager.getConnection(url); //same

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
