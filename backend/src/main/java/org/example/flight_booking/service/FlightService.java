package org.example.flight_booking.service;

import org.example.flight_booking.dto.FlightData;
import org.example.flight_booking.model.Flight;
import org.example.flight_booking.repository.FlightRepository;
import org.example.flight_booking.specification.FlightSpecification;
import org.example.flight_booking.utils.DateTimeUtil;
import org.example.flight_booking.utils.SeatPlanGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class FlightService {
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final FlightApiService flightApiService;
    private final IataCityService iataCityService;
    private final FlightPricingService flightPricingService;
    private final AircraftDataService aircraftDataService;
    private final SeatOccupancyService seatOccupancyService;
    private final Random random = new Random();

    @Value("${flight.schedule.days}")
    private int scheduledDays;

    public FlightService(FlightRepository flightRepository,
                         FlightApiService flightApiService,
                         IataCityService iataCityService,
                         FlightPricingService flightPricingService,
                         AircraftDataService aircraftDataService,
                         SeatOccupancyService seatOccupancyService) {
        this.flightRepository = flightRepository;
        this.flightApiService = flightApiService;
        this.iataCityService = iataCityService;
        this.flightPricingService = flightPricingService;
        this.aircraftDataService = aircraftDataService;
        this.seatOccupancyService = seatOccupancyService;
    }

    public Page<Flight> findFlights(LocalDate startDate, LocalDate endDate,
                                    String departure, String destination,
                                    Integer minDuration, Integer maxDuration,
                                    Double minPrice, Double maxPrice,
                                    Pageable pageable) {
        return flightRepository.findAll(FlightSpecification.withFilters(startDate, endDate, departure, destination, minDuration, maxDuration, minPrice, maxPrice), pageable);
    }

    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    public Optional<Flight> getFlightById(Long flightId) {
        return flightRepository.findById(flightId);
    }

    public List<String> findAllAirports() {
        return flightRepository.findAllUniqueAirportIataCodes();
    }

    public List<Object[]> findAllAircrafts() {
        return flightRepository.findAircraftTypesWithCount();
    }

    public void fetchAndSaveFlights() {
        List<FlightData> flightsFromApi = flightApiService.fetchFlights();
        log.info("Processing {} flights from API.", flightsFromApi.size());

        int savedFlightsCount = 0;

        for (FlightData apiFlight : flightsFromApi) {
            if (!validateFlightData(apiFlight)) {
                continue;
            }

            int randomDays = random.nextInt(scheduledDays) + 1;

            ZonedDateTime departureTime = DateTimeUtil.convertToZonedTime(apiFlight.getDeparture().getScheduled(), apiFlight.getDeparture().getTimezone());
            ZonedDateTime arrivalTime = DateTimeUtil.convertToZonedTime(apiFlight.getArrival().getScheduled(), apiFlight.getArrival().getTimezone());
            int durationMinutes = (int) java.time.Duration.between(departureTime, arrivalTime).toMinutes();
            String aircraftType;

            if (apiFlight.getAircraft() != null &&
                    aircraftDataService.isValidAircraftType(apiFlight.getAircraft().getIata())){
                aircraftType = apiFlight.getAircraft().getIata();
            } else{
                aircraftType = aircraftDataService.findBestAircraft(durationMinutes);
            }

            Flight flight = new Flight(
                    apiFlight.getFlight().getIata(),
                    aircraftType,
                    apiFlight.getDeparture().getIata(),
                    apiFlight.getArrival().getIata(),
                    iataCityService.getCityName(apiFlight.getDeparture().getIata()),
                    iataCityService.getCityName(apiFlight.getArrival().getIata()),
                    departureTime.plusDays(randomDays),
                    arrivalTime.plusDays(randomDays),
                    durationMinutes,
                    flightPricingService.calculateInitialPrice(durationMinutes),
                    flightPricingService.getExtraLegroomMultiplier(),
                    flightPricingService.getNearExitMultiplier()
            );

            flight.setSeatPlan(SeatPlanGenerator.generateFromFile(aircraftDataService.getSeatPlanFile(aircraftType)));
            seatOccupancyService.fillSeatsRandomly(flight);

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
