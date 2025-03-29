package org.example.flight_booking.service;

import org.example.flight_booking.dto.FlightData;
import org.example.flight_booking.model.Flight;
import org.example.flight_booking.model.Seat;
import org.example.flight_booking.repository.FlightRepository;
import org.example.flight_booking.specification.FlightSpecification;
import org.example.flight_booking.utils.DateTimeUtil;
import org.example.flight_booking.utils.SeatPlanGenerator;
import org.example.flight_booking.utils.SeatRecommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * Find flights with filters
     *
     * @param startDate   the start date
     * @param endDate     the end date
     * @param departure   the departure airport
     * @param destination the destination airport
     * @param minDuration the minimum duration
     * @param maxDuration the maximum duration
     * @param minPrice    the minimum price
     * @param maxPrice    the maximum price
     * @param pageable    the pageable object
     * @return a page of flights
     */
    public Page<Flight> findFlights(LocalDate startDate, LocalDate endDate,
                                    String departure, String destination,
                                    Integer minDuration, Integer maxDuration,
                                    Double minPrice, Double maxPrice,
                                    Pageable pageable) {
        return flightRepository.findAll(FlightSpecification.withFilters(startDate, endDate, departure, destination, minDuration, maxDuration, minPrice, maxPrice), pageable);
    }

    /**
     * Check if a flight with the given flight number exists
     *
     * @param flightNumber the flight number
     * @return true if a flight with the given flight number exists, false otherwise
     */
    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    /**
     * Get a flight by ID
     *
     * @param flightId the flight ID
     * @return an optional of the flight
     */
    public Optional<Flight> getFlightById(Long flightId) {
        return flightRepository.findById(flightId);
    }

    /**
     * Find all unique airport IATA codes
     *
     * @return a list of unique airport IATA codes
     */
    public List<String> findAllAirports() {
        return flightRepository.findAllUniqueAirportIataCodes();
    }

    /**
     * Find all unique aircraft types
     *
     * @return a list of unique aircraft types
     */
    public List<Object[]> findAllAircrafts() {
        return flightRepository.findAircraftTypesWithCount();
    }

    /**
     * Recommend seats for a flight
     *
     * @param flightId      the flight ID
     * @param numSeats      the number of seats to recommend
     * @param window        true if window seat is preferred
     * @param aisle         true if aisle seat is preferred
     * @param extraLegroom  true if extra legroom seat is preferred
     * @param nearExit      true if seat near exit is preferred
     * @param adjacent      true if adjacent seats are preferred
     * @param selectedSeats the list of selected seats
     * @return a list of recommended seats
     */
    public List<Seat> recommendSeats(Long flightId, int numSeats, boolean window, boolean aisle, boolean extraLegroom, boolean nearExit, boolean adjacent, List<Long> selectedSeats) {
        Optional<Flight> flight = getFlightById(flightId);
        if (flight.isEmpty()) {
            return List.of();
        }

        return SeatRecommendation.recommendSeats(flight.get().getSeatPlan(), numSeats, window, aisle, extraLegroom, nearExit, adjacent, selectedSeats);
    }

    /**
     * Find cities based on type and query
     *
     * @param type  the type of city
     * @param query the query (start of a city name) to filter the cities
     * @return a list of cities
     */
    public List<String> findCities(String type, String query) {
        Pageable limit = PageRequest.of(0, 10);
        if (type.equals("departure")) {
            return flightRepository.findDepartureCities(query, limit);
        } else if (type.equals("destination")) {
            return flightRepository.findDestinationCities(query, limit);
        } else {
            return List.of();
        }
    }

    /**
     * Fetch flights from the API and save them to the database
     */
    public void fetchAndSaveFlights() {
        List<FlightData> flightsFromApi = flightApiService.fetchFlights();
        log.info("Processing {} flights from API.", flightsFromApi.size());

        int savedFlightsCount = 0;

        for (FlightData apiFlight : flightsFromApi) {
            // validate flight data
            if (!validateFlightData(apiFlight)) {
                continue;
            }

            // add random days to the scheduled date to simulate different days
            int randomDays = random.nextInt(scheduledDays) + 1;

            ZonedDateTime departureTime = DateTimeUtil.convertToZonedTime(apiFlight.getDeparture().getScheduled(), apiFlight.getDeparture().getTimezone());
            ZonedDateTime arrivalTime = DateTimeUtil.convertToZonedTime(apiFlight.getArrival().getScheduled(), apiFlight.getArrival().getTimezone());
            int durationMinutes = (int) java.time.Duration.between(departureTime, arrivalTime).toMinutes();
            String aircraftType;

            // check if aircraft type is valid and use it, otherwise find the best aircraft type based on duration
            if (apiFlight.getAircraft() != null &&
                    aircraftDataService.isValidAircraftType(apiFlight.getAircraft().getIata())) {
                aircraftType = apiFlight.getAircraft().getIata();
            } else {
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

            // generate seat plan and fill seats randomly
            flight.setSeatPlan(SeatPlanGenerator.generateFromFile(aircraftDataService.getSeatPlanFile(aircraftType)));
            seatOccupancyService.fillSeatsRandomly(flight);

            flightRepository.save(flight);
            savedFlightsCount++;

            log.debug("Saved flight {} from {} to {}.", flight.getFlightNumber(), flight.getDepartureIATA(), flight.getDestinationIATA());
        }

        log.info("Finished processing API flights. {} new flights saved to database.", savedFlightsCount);
    }

    /**
     * Validate flight data
     *
     * @param apiFlight the flight data from the API
     * @return true if the flight data is valid, false otherwise
     */
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
