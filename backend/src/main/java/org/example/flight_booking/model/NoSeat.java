package org.example.flight_booking.model;

public class NoSeat extends Seat{
    public NoSeat(long flightId) {
        super(flightId, "_", true, false, false, false, false);
    }
}
