package org.example.flight_booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightData {
    private Departure departure;
    private Arrival arrival;
    private FlightInfo flight;
    private Aircraft aircraft;

    public Departure getDeparture() {
        return departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public FlightInfo getFlight() {
        return flight;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}
