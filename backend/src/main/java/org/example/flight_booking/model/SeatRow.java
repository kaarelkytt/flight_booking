package org.example.flight_booking.model;

import java.util.List;

public class SeatRow {
    private long id;
    private List<Seat> seats;

    public SeatRow(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "SeatRow{" +
                "seats=" + seats +
                '}' +
                '\n';
    }
}
