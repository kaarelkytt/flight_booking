package org.example.flight_booking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SeatPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @OneToMany(mappedBy = "seatPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SeatRow> seatRows = new ArrayList<>();

    public SeatPlan() {
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<SeatRow> getSeatRows() {
        return seatRows;
    }

    public void addSeatRow(SeatRow row) {
        row.setSeatPlan(this);
        seatRows.add(row);
    }

    public List<Seat> findAllFreeSeats() {
        List<Seat> seats = new ArrayList<>();
        for (SeatRow row : seatRows) {
            for (Seat seat : row.getSeats()) {
                if (!(seat instanceof NoSeat) && !seat.isOccupied()) {
                    seats.add(seat);
                }
            }
        }
        return seats;
    }

    @Override
    public String toString() {
        return "SeatPlan{" +
                "id=" + id +
                ", seatRows=\n" + seatRows +
                '}';
    }
}
