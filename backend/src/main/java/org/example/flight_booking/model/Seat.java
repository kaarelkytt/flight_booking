package org.example.flight_booking.model;

public class Seat {
    private long id;

    private String seatNumber;
    private boolean occupied;
    private boolean aisleSeat;
    private boolean windowSeat;
    private boolean extraLegroom;
    private boolean nearExit;

    public Seat(String seatNumber, boolean aisleSeat, boolean windowSeat, boolean extraLegroom, boolean nearExit) {
        this.seatNumber = seatNumber;
        this.occupied = false;
        this.aisleSeat = aisleSeat;
        this.windowSeat = windowSeat;
        this.extraLegroom = extraLegroom;
        this.nearExit = nearExit;
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

    public void setExtraLegroom(boolean extraLegroom) {
        this.extraLegroom = extraLegroom;
    }

    public boolean isNearExit() {
        return nearExit;
    }

    public void setNearExit(boolean nearExit) {
        this.nearExit = nearExit;
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
