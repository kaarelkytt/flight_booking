package org.example.flight_booking.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat_row_id")
    private SeatRow seatRow;

    private String seatNumber;
    private boolean occupied;
    private boolean aisleSeat;
    private boolean windowSeat;
    private boolean extraLegroom;
    private boolean nearExit;

    public Seat(String seatNumber, boolean occupied, boolean aisleSeat, boolean windowSeat, boolean extraLegroom, boolean nearExit) {
        this.seatNumber = seatNumber;
        this.occupied = occupied;
        this.aisleSeat = aisleSeat;
        this.windowSeat = windowSeat;
        this.extraLegroom = extraLegroom;
        this.nearExit = nearExit;
    }

    public Seat(String seatNumber, boolean aisleSeat, boolean windowSeat, boolean extraLegroom, boolean nearExit) {
        this(seatNumber, false, aisleSeat, windowSeat, extraLegroom, nearExit);
    }

    public Seat() {
    }

    public long getId() {
        return id;
    }

    public void setSeatRow(SeatRow seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isAisleSeat() {
        return aisleSeat;
    }

    public void setAisleSeat(boolean aisleSeat) {
        this.aisleSeat = aisleSeat;
    }

    public boolean isWindowSeat() {
        return windowSeat;
    }

    public void setWindowSeat(boolean windowSeat) {
        this.windowSeat = windowSeat;
    }

    public boolean isExtraLegroom() {
        return extraLegroom;
    }

    public boolean isNearExit() {
        return nearExit;
    }

    @Override
    public String toString() {
        return '[' +
                seatNumber + " " +
                (aisleSeat ? "A" : " ") + " " +
                (windowSeat ? "W" : " ") + " " +
                (extraLegroom ? "E" : " ") + " " +
                (nearExit ? "N" : " ") +
                ']';

    }
}
