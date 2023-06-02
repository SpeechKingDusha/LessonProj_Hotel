package org.Amdocs;

import org.Amdocs.views.HotelBookingSystem;

public class Main {
    public static void main(String[] args) {
        HotelBookingSystem app = new HotelBookingSystem();
        app.setTitle("Hotel Booking System");
        app.setSize(800, 500);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}
