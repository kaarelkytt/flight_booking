package org.example.flight_booking.model;

import java.util.List;

public class SeatPlan {
    private long id;
    private List<SeatRow> seatRows;

    public SeatPlan(List<SeatRow> seatRows) {
        this.seatRows = seatRows;
    }

    @Override
    public String toString() {
        return "SeatPlan{" +
                "id=" + id +
                ", seatRows=\n" + seatRows +
                '}';
    }
}
