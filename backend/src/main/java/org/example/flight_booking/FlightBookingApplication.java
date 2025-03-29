package org.example.flight_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlightBookingApplication {

    /**
     * Main method to run the application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
    }

}
