package org.Amdocs;

import org.Amdocs.repository.DBRep;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.List;

class HotelBookingSystem extends JFrame {

    //private final Connection conn = DBRep.conn;
    private JTable table;
    private Box contents = new Box(BoxLayout.Y_AXIS);
    private JTextField empId, empName, department, salary, dtJoin, emailId;
    // Column name
    private final static String[] COLUMNS = {"ID", "GuestName", "ContactNo", "Room", "Check_in", "Check_out", "EMailID", "SumGuests"};
    private final static String[] ROOMS = {"Simple", "Deluxe", "SuperDeluxe"};

    public HotelBookingSystem() {
        DBRep.getConnection();

        //conn = getConnection();
        table = new JTable();
        List<PeopleInfo> rawData = DBRep.getRawDataFromDB(COLUMNS);
        DefaultTableModel model = getModel(rawData);
        drawTable(model);
        drawComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    DBRep.conn.close();
                    System.out.println("Connection to SQLite has been closed.");
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
                System.exit(0);
            }
        });
    }

    private void refreshData() {
        List<PeopleInfo> rawData = DBRep.getRawDataFromDB(COLUMNS);
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
                JComboBox room = new JComboBox<>(ROOMS);
                JTextField checkIn = new JTextField();
                JTextField checkOut = new JTextField();
                JTextField emailId = new JTextField();
                JTextField sumGuests = new JTextField();
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
                        new JLabel(COLUMNS[7]),
                        sumGuests
                };


                int result = JOptionPane.showConfirmDialog(null, inputs, "Do you want add new guest?", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    PeopleInfo people = new PeopleInfo();
                    people.setGuestName(guestName.getText());
                    people.setContactNo(contact.getText());
                    people.setRoom((String) (room.getSelectedItem()));
                    people.setCheckIn(checkIn.getText());
                    people.setCheckOut(checkOut.getText());
                    people.setEmailID(emailId.getText());
                    people.setSumGuests(Integer.parseInt(sumGuests.getText()));

                    DBRep.insert(people);

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
                    Integer sumGuestsValue = (Integer) (table.getValueAt(table.getSelectedRow(), 7));

                    JTextField guestName = new JTextField();
                    JTextField contact = new JTextField();
                    JComboBox room = new JComboBox<>(ROOMS);
                    JTextField checkIn = new JTextField();
                    JTextField checkOut = new JTextField();
                    JTextField emailId = new JTextField();
                    JTextField sumGuests = new JTextField();

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
                            new JLabel(COLUMNS[7]),
                            sumGuests
                    };
                    guestName.setText(guestNameValue);
                    contact.setText(guestNoValue);
                    room.setSelectedItem(roomValue);
                    checkIn.setText(checkInValue);
                    checkOut.setText(checkOutValue);
                    emailId.setText(emailIdValue);
                    sumGuests.setText(sumGuestsValue.toString());

                    Validator validator = new Validator();
                    PeopleInfo people = new PeopleInfo();
                    while (validator == null || !validator.isGood()) {
                        int result = JOptionPane.showConfirmDialog(HotelBookingSystem.this, inputs, "Update dialog", JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {

                            people.setGuestName(guestName.getText());
                            people.setContactNo(contact.getText());
                            people.setRoom((String) (room.getSelectedItem()));
                            people.setCheckIn(checkIn.getText());
                            people.setCheckOut(checkOut.getText());
                            people.setEmailID(emailId.getText());
                            people.setSumGuests(Integer.parseInt(sumGuests.getText()));
                            people.setGuestId(guestIdValue);

                            validator = Validator.validate(people, COLUMNS);
                            if (validator.getEmptyFields().size()>0){
                                if (validator.getEmptyFields().contains(COLUMNS[1])){
                                    guestName.setBackground(Color.ORANGE);
                                }
                                else guestName.setBackground(Color.white);
                                if (validator.getEmptyFields().contains(COLUMNS[2])){
                                    contact.setBackground(Color.ORANGE);
                                }
                                else contact.setBackground(Color.white);
                                if (validator.getEmptyFields().contains(COLUMNS[6])){
                                    emailId.setBackground(Color.ORANGE);
                                }
                                else emailId.setBackground(Color.white);
                                if (validator.getEmptyFields().contains(COLUMNS[7])){
                                    sumGuests.setBackground(Color.ORANGE);
                                }
                                else sumGuests.setBackground(Color.white);
                                JOptionPane.showMessageDialog(HotelBookingSystem.this, validator.getMessage(), "Fields check", JOptionPane.WARNING_MESSAGE);
                            }
                            DBRep.update(people);

                        } else {
                            System.out.println("User canceled / closed the dialog, result = " + result);
                        }
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

                        DBRep.delete(guestId);
                        System.out.println("Item deleted successfully");
                        refreshData();
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
            Object[] items = new Object[8];
            items[0] = people.getId();
            items[1] = people.getGuestName();
            items[2] = people.getContactNo();
            items[3] = people.getRoom();
            items[4] = people.getCheckIn();
            items[5] = people.getCheckOut();
            items[6] = people.getEmailID();
            items[7] = people.getSumGuests();
            model.addRow(items);
        }
        return model;
    }

}
