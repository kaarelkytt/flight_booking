package org.example.flight_booking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SeatRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat_plan_id")
    private SeatPlan seatPlan;

    @OneToMany(mappedBy = "seatRow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    public SeatRow() {
    }

    public void setSeatPlan(SeatPlan seatPlan) {
        this.seatPlan = seatPlan;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeat(Seat seat) {
        seat.setSeatRow(this);
        seats.add(seat);
    }

    @Override
    public String toString() {
        return "SeatRow{" +
                "seats=" + seats +
                '}' +
                '\n';
    }
}
