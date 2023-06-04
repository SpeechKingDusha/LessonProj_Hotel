package org.Amdocs.models;

public class PeopleInfo {
    public final static String[] ROOMS = {"Simple", "Deluxe", "SuperDeluxe"};
    private Integer id;
    private String guestName;
    private String contactNo;
    private String room=ROOMS[0];
    private String checkIn;
    private String checkOut;
    private String emailID;
    private Integer sumGuests=1;

    public Integer getId() {
        return id;
    }

    public void setGuestId(Integer id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    @Override
    public String toString() {
        return "PeopleInfo{" +
                "guestId='" + id + '\'' +
                ", guestName='" + guestName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", room='" + room + '\'' +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                ", emailID='" + emailID + '\'' +
                ", sumGuests='" + sumGuests + '\'' +
                '}';
    }

    public Integer getSumGuests() {
        return sumGuests;
    }

    public void setSumGuests(Integer sumGuests) {
        this.sumGuests = sumGuests;
    }
}
