package org.example.flight_booking.dto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Departure {
    private static final Logger log = LoggerFactory.getLogger(Departure.class);
    private String iata;
    private String scheduled;
    private String timezone;

    public String getIata() {
        return iata;
    }

    public String getScheduled() {
        return scheduled;
    }

    public String getTimezone() {
        return timezone;
    }
}
