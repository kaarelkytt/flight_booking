package org.example.flight_booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FlightDataUpdater {
    private static final Logger log = LoggerFactory.getLogger(FlightDataUpdater.class);
    private final FlightService flightService;

    @Value("${flightdata.update.enabled}")
    private boolean isUpdateEnabled;

    public FlightDataUpdater(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Scheduled method to update flight data.
     */
    @Scheduled(fixedRateString = "${flightdata.update.rate}")
    public void updateFlightData() {
        if (!isUpdateEnabled) {
            log.info("Flight data update is disabled.");
            return;
        }

        log.info("Starting scheduled flight data update...");
        flightService.fetchAndSaveFlights();
        log.info("Flight data update completed.");
    }

}
