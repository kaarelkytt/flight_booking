package org.example.flight_booking.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SeatPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany
    private List<SeatRow> seatRows;

    public SeatPlan(List<SeatRow> seatRows) {
        this.seatRows = seatRows;
    }

    public SeatPlan() {

    }

    public List<SeatRow> getSeatRows() {
        return seatRows;
    }

    @Override
    public String toString() {
        return "SeatPlan{" +
                "id=" + id +
                ", seatRows=\n" + seatRows +
                '}';
    }
}
