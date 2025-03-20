package org.example.flight_booking.service;

import org.example.flight_booking.dto.FlightData;
import org.example.flight_booking.model.Flight;
import org.example.flight_booking.repository.FlightRepository;
import org.example.flight_booking.specification.FlightSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

import static org.example.flight_booking.utils.DateTimeUtil.convertToZonedTime;

@Service
public class FlightService {
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final FlightApiService flightApiService;
    private final SeatPlanGenerator seatPlanGenerator;
    private final IataCityService iataCityService;
    private final FlightPricingService flightPricingService;
    private final Random random = new Random();

    public FlightService(FlightRepository flightRepository,
                         FlightApiService flightApiService,
                         SeatPlanGenerator seatPlanGenerator,
                         IataCityService iataCityService,
                         FlightPricingService flightPricingService) {
        this.flightRepository = flightRepository;
        this.flightApiService = flightApiService;
        this.seatPlanGenerator = seatPlanGenerator;
        this.iataCityService = iataCityService;
        this.flightPricingService = flightPricingService;
    }

    public List<Flight> findFlights(LocalDate startDate, LocalDate endDate,
                                    String departure, String destination,
                                    Integer minDuration, Integer maxDuration,
                                    Double minPrice, Double maxPrice) {
        return flightRepository.findAll(FlightSpecification.withFilters(startDate, endDate, departure, destination, minDuration, maxDuration, minPrice, maxPrice));
    }

    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    public List<String> findAllAirports() {
        return flightRepository.findAllUniqueAirportIataCodes();
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

            ZonedDateTime departureTime = convertToZonedTime(apiFlight.getDeparture().getScheduled(), apiFlight.getDeparture().getTimezone());
            ZonedDateTime arrivalTime = convertToZonedTime(apiFlight.getArrival().getScheduled(), apiFlight.getArrival().getTimezone());
            int durationMinutes = (int) java.time.Duration.between(departureTime, arrivalTime).toMinutes();

            Flight flight = new Flight(
                    apiFlight.getFlight().getIata(),
                    apiFlight.getAircraft() != null ? apiFlight.getAircraft().getIata() : "UNKNOWN",
                    apiFlight.getDeparture().getIata(),
                    apiFlight.getArrival().getIata(),
                    iataCityService.getCityName(apiFlight.getDeparture().getIata()),
                    iataCityService.getCityName(apiFlight.getArrival().getIata()),
                    departureTime.plusDays(randomDays),
                    arrivalTime.plusDays(randomDays),
                    durationMinutes,
                    flightPricingService.calculateInitialPrice(durationMinutes)
            );
            seatPlanGenerator.generateSeatPlan(flight);
            // TODO - add occupied seats
            flightRepository.save(flight);
            savedFlightsCount++;

            log.debug("Saved flight {} from {} to {}.", flight.getFlightNumber(), flight.getDepartureIATA(), flight.getDestinationIATA());
        }

        log.info("Finished processing API flights. {} new flights saved to database.", savedFlightsCount);
    }

    private boolean validateFlightData(FlightData apiFlight) {
        if (apiFlight.getFlight().getIata() == null ||
                apiFlight.getDeparture().getIata() == null ||
                apiFlight.getArrival().getIata() == null) {
            log.info("Skipping flight because of missing IATA code.");
            return false;
        }

        if (existsByFlightNumber(apiFlight.getFlight().getIata())) {
            log.info("Flight {} already exists in the database.", apiFlight.getFlight().getIata());
            return false;
        }

        if (apiFlight.getDeparture().getScheduled() == null || apiFlight.getArrival().getScheduled() == null) {
            log.info("Skipping flight {} because of missing scheduled time.", apiFlight.getFlight().getIata());
            return false;
        }

        if (apiFlight.getDeparture().getTimezone() == null || apiFlight.getArrival().getTimezone() == null) {
            log.info("Skipping flight {} because of missing timezone.", apiFlight.getFlight().getIata());
            return false;
        }

        if (iataCityService.getCityName(apiFlight.getDeparture().getIata()).equals("UNKNOWN") ||
                iataCityService.getCityName(apiFlight.getArrival().getIata()).equals("UNKNOWN")) {
            log.info("Skipping flight {} because of missing city.", apiFlight.getFlight().getIata());
            return false;
        }

        return true;
    }
}
