package org.Amdocs.repository;

import org.Amdocs.models.PeopleInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRep {
    private static final String DBDRIVERNAME = "jdbc:sqlite:";
    private static final String DBNAME = "./sqlite.db";
    public static Connection conn;

    public static void getConnection() {
        //Connection conn = null; //same
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(DBDRIVERNAME + DBRep.class.getClassLoader().getResource(DBNAME)); //same

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static ResultSet select(String tableName, String orderByColumn) {
        StringBuilder querySQL = new StringBuilder("SELECT * FROM " + tableName + " ORDER BY " + orderByColumn + " ASC");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeQuery(querySQL.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void insert(PeopleInfo people) {
        String sql = "INSERT INTO HotelTable values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setString(1, empId.getText());
            ps.setString(2, people.getGuestName());
            ps.setString(3, people.getContactNo());
            ps.setString(4, people.getRoom());
            ps.setString(5, people.getCheckIn());
            ps.setString(6, people.getCheckOut());
            ps.setString(7, people.getEmailID());
            ps.setInt (8, people.getSumGuests());
            //This method is when update/insert/delete any row
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void update(PeopleInfo people) {
        String sql = "UPDATE HotelTable SET GuestName = ?, ContactNo = ?, Room = ?, Check_in = ?, Check_out = ?, EMailID = ?, SumGuests = ? WHERE Id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, people.getGuestName());
            ps.setString(2, people.getContactNo());
            ps.setString(3, people.getRoom());
            ps.setString(4, people.getCheckIn());
            ps.setString(5, people.getCheckOut());
            ps.setString(6, people.getEmailID());
            ps.setInt(7, people.getSumGuests());
            ps.setInt(8, people.getId());
            //This method is when update/insert/delete any row
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void delete(int guestId) {
        try {
            String sql = "DELETE FROM HotelTable WHERE Id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, guestId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<PeopleInfo> getRawDataFromDB(String[] COLUMNS) {
        List<PeopleInfo> rawData = new ArrayList<>();
        try {
            var peoples = DBRep.select("HotelTable", "Id");
            while (peoples.next()) {
                PeopleInfo people = new PeopleInfo();
                people.setGuestId(peoples.getInt(COLUMNS[0]));
                people.setGuestName(peoples.getString(COLUMNS[1]));
                people.setContactNo(peoples.getString(COLUMNS[2]));
                people.setRoom(peoples.getString(COLUMNS[3]));
                people.setCheckIn(peoples.getString(COLUMNS[4]));
                people.setCheckOut(peoples.getString(COLUMNS[5]));
                people.setEmailID(peoples.getString(COLUMNS[6]));
                people.setSumGuests(peoples.getInt (COLUMNS[7]));
                rawData.add(people);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rawData;
    }
}
