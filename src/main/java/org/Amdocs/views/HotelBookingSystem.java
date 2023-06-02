package org.Amdocs.views;

import org.Amdocs.models.PeopleInfo;
import org.Amdocs.repository.DBRep;
import org.Amdocs.services.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.List;

public class HotelBookingSystem extends JFrame {

    private JTable table;
    private Box contents = new Box(BoxLayout.Y_AXIS);
    private final static String[] COLUMNS = {"ID", "GuestName", "ContactNo", "Room", "Check_in", "Check_out", "EMailID", "SumGuests"};
    private final static String[] ROOMS = {"Simple", "Deluxe", "SuperDeluxe"};

    public HotelBookingSystem() {
        DBRep.getConnection();

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
                PeopleInfo newPeople = new PeopleInfo();
                drawEditeForm(newPeople);
            }
        });
        contentsButton.add(newModalButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() > -1) {
                    PeopleInfo selectedPeople = new PeopleInfo();

                    selectedPeople.setGuestId((Integer) (table.getValueAt(table.getSelectedRow(), 0)));
                    selectedPeople.setGuestName((String) (table.getValueAt(table.getSelectedRow(), 1)));
                    selectedPeople.setContactNo((String) (table.getValueAt(table.getSelectedRow(), 2)));
                    selectedPeople.setRoom((String) (table.getValueAt(table.getSelectedRow(), 3)));
                    selectedPeople.setCheckIn((String) (table.getValueAt(table.getSelectedRow(), 4)));
                    selectedPeople.setCheckOut((String) (table.getValueAt(table.getSelectedRow(), 5)));
                    selectedPeople.setEmailID((String) (table.getValueAt(table.getSelectedRow(), 6)));
                    selectedPeople.setSumGuests((Integer) (table.getValueAt(table.getSelectedRow(), 7)));

                    drawEditeForm(selectedPeople);

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

    private void drawEditeForm(PeopleInfo peopleOld) {

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

        guestName.setText(peopleOld.getGuestName());
        contact.setText(peopleOld.getContactNo());
        room.setSelectedItem(peopleOld.getRoom());
        checkIn.setText(peopleOld.getCheckIn());
        checkOut.setText(peopleOld.getCheckOut());
        emailId.setText(peopleOld.getEmailID());
        if (peopleOld.getSumGuests() != null) {
            sumGuests.setText(peopleOld.getSumGuests().toString());
        }
        Validator validator = new Validator();

        while (validator == null || !validator.isGood()) {
            int result = JOptionPane.showConfirmDialog(HotelBookingSystem.this, inputs, "Update dialog", JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.CLOSED_OPTION) {
                break;
            }
            if (result == JOptionPane.OK_OPTION) {

                peopleOld.setGuestName(guestName.getText());
                peopleOld.setContactNo(contact.getText());
                peopleOld.setRoom((String) (room.getSelectedItem()));
                peopleOld.setCheckIn(checkIn.getText());
                peopleOld.setCheckOut(checkOut.getText());
                peopleOld.setEmailID(emailId.getText());
                peopleOld.setSumGuests(Integer.parseInt(sumGuests.getText()));

                validator = Validator.validate(peopleOld, COLUMNS);
                if (validator.getBrokenFields().size() > 0) {
                    if (validator.getBrokenFields().contains(COLUMNS[1])) {
                        guestName.setBackground(Color.ORANGE);
                    } else guestName.setBackground(Color.white);
                    if (validator.getBrokenFields().contains(COLUMNS[2])) {
                        contact.setBackground(Color.ORANGE);
                    } else contact.setBackground(Color.white);
                    if (validator.getBrokenFields().contains(COLUMNS[6])) {
                        emailId.setBackground(Color.ORANGE);
                    } else emailId.setBackground(Color.white);
                    if (validator.getBrokenFields().contains(COLUMNS[7])) {
                        sumGuests.setBackground(Color.ORANGE);
                    } else sumGuests.setBackground(Color.white);
                    JOptionPane.showMessageDialog(HotelBookingSystem.this, validator.getMessage(), "Fields check", JOptionPane.WARNING_MESSAGE);
                }
                if (validator.isGood()) {
                    if (peopleOld.getId() != null) {
                        DBRep.update(peopleOld);
                    } else {
                        DBRep.insert(peopleOld);
                    }
                }

            } else {
                System.out.println("User canceled / closed the dialog, result = " + result);
            }
        }
        refreshData();
    }

}
