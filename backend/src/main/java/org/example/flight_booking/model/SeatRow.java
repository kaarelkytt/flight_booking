package org.example.flight_booking.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SeatRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany
    private List<Seat> seats;

    public SeatRow(List<Seat> seats) {
        this.seats = seats;
    }

    public SeatRow() {

    }

    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "SeatRow{" +
                "seats=" + seats +
                '}' +
                '\n';
    }
}
