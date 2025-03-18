package org.example.flight_booking.model;

public class NoSeat extends Seat{
    public NoSeat(long flightId) {
        super(flightId, "_", false, false, false, false);
    }
}
