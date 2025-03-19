package org.example.flight_booking.service;

import org.example.flight_booking.dto.FlightData;
import org.example.flight_booking.model.Flight;
import org.example.flight_booking.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

@Service
public class FlightService {
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final FlightApiService flightApiService;
    private final SeatPlanGenerator seatPlanGenerator;
    private final Random random = new Random();

    public FlightService(FlightRepository flightRepository, FlightApiService flightApiService, SeatPlanGenerator seatPlanGenerator) {
        this.flightRepository = flightRepository;
        this.flightApiService = flightApiService;
        this.seatPlanGenerator = seatPlanGenerator;
    }

    public List<Flight> findFlights(String departure) {
        return flightRepository.findByDepartureIATA(departure);
    }

    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    public void fetchAndSaveFlights() {
        List<FlightData> flightsFromApi = flightApiService.fetchFlights();
        log.info("Processing {} flights from API.", flightsFromApi.size());

        int savedFlightsCount = 0;

        for (FlightData apiFlight : flightsFromApi) {
            if (!validateFlightData(apiFlight)) {
                continue;
            }

            int randomDays = random.nextInt(30) + 1;
            Flight flight = new Flight(
                    apiFlight.getFlight().getIata(),
                    apiFlight.getDeparture().getIata(),
                    apiFlight.getArrival().getIata(),
                    apiFlight.getDeparture().getTimezone(),
                    apiFlight.getArrival().getTimezone(),
                    OffsetDateTime.parse(apiFlight.getDeparture().getScheduled()).plusDays(randomDays),
                    OffsetDateTime.parse(apiFlight.getArrival().getScheduled()).plusDays(randomDays),
                    apiFlight.getAircraft() != null ? apiFlight.getAircraft().getIata() : "UNKNOWN"
            );
            seatPlanGenerator.generateSeatPlan(flight);
            flightRepository.save(flight);
            savedFlightsCount++;

            log.debug("Saved flight {} from {} to {}.", flight.getFlightNumber(), flight.getDepartureIATA(), flight.getDestinationIATA());
        }

        log.info("Finished processing API flights. {} new flights saved to database.", savedFlightsCount);
    }

    private boolean validateFlightData(FlightData apiFlight) {
        if (existsByFlightNumber(apiFlight.getFlight().getIata())) {
            log.info("Flight {} already exists in the database.", apiFlight.getFlight().getIata());
            return false;
        }

        if (apiFlight.getFlight().getIata() == null || apiFlight.getDeparture().getIata() == null || apiFlight.getArrival().getIata() == null) {
            log.info("Skipping flight because of missing IATA code.");
            return false;
        }

        if (apiFlight.getDeparture().getScheduled() == null || apiFlight.getArrival().getScheduled() == null) {
            log.info("Skipping flight {} because of missing scheduled time.", apiFlight.getFlight().getIata());
            return false;
        }

        if (apiFlight.getDeparture().getTimezone().equals("UNKNOWN") || apiFlight.getArrival().getTimezone().equals("UNKNOWN")) {
            log.info("Skipping flight {} because of missing timezone.", apiFlight.getFlight().getIata());
            return false;
        }

        return true;
    }

    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }
}
