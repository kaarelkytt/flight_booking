package org.example.flight_booking.controller;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;
import org.example.flight_booking.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    /**
     * Constructor
     *
     * @param flightService the flight service
     */
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Endpoint to get a list of all airports
     *
     * @return a list of all airports
     */
    @GetMapping("/airports")
    public ResponseEntity<List<String>> findAllAirports() {
        return ResponseEntity.ok(flightService.findAllAirports());
    }

    /**
     * Endpoint to get a list of all aircraft types
     *
     * @return a list of all aircraft types
     */
    @GetMapping("/aircrafts")
    public ResponseEntity<List<Object[]>> findAllAircrafts() {
        return ResponseEntity.ok(flightService.findAllAircrafts());
    }

    /**
     * Endpoint to get a seat plan for a flight
     *
     * @param flightId the flight id
     * @return a seat plan for the flight
     */
    @CrossOrigin
    @GetMapping("/{flightId}/seats")
    public ResponseEntity<SeatPlan> findSeatPlan(@PathVariable Long flightId) {
        Optional<Flight> flight = flightService.getFlightById(flightId);
        if (flight.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(flight.get().getSeatPlan());
        }
    }

    /**
     * Endpoint to get a list of cities based on type (departure or destination) and query (start of a city name)
     *
     * @param type  the type of city (departure or destination)
     * @param query the query to filter the cities
     * @return a list of cities
     */
    @CrossOrigin
    @GetMapping("/{type}/cities")
    public ResponseEntity<List<String>> findCities(
            @PathVariable String type,
            @RequestParam(required = false) String query) {

        return ResponseEntity.ok(flightService.findCities(type, query));
    }

    /**
     * Endpoint to recommend seats for a flight
     *
     * @param flightId      the flight id
     * @param numSeats      the number of seats to recommend
     * @param window        whether the seat should be a window seat
     * @param aisle         whether the seat should be an aisle seat
     * @param extraLegroom  whether the seat should have extra legroom
     * @param nearExit      whether the seat should be near an exit
     * @param adjacent      whether the seats should be adjacent
     * @param selectedSeats the list of already selected seats
     * @return a list of recommended seats
     */
    @CrossOrigin
    @PostMapping("/{flightId}/recommend-seats")
    public ResponseEntity<List<Seat>> recommendSeats(
            @PathVariable Long flightId,
            @RequestParam int numSeats,
            @RequestParam boolean window,
            @RequestParam boolean aisle,
            @RequestParam boolean extraLegroom,
            @RequestParam boolean nearExit,
            @RequestParam boolean adjacent,
            @RequestBody List<Long> selectedSeats) {
        List<Seat> seats = flightService.recommendSeats(flightId, numSeats, window, aisle, extraLegroom, nearExit, adjacent, selectedSeats);
        return ResponseEntity.ok(seats);
    }

    /**
     * Endpoint to get a list of flights
     *
     * @param page       the page number
     * @param size       the page size
     * @param sortField  the field to sort by
     * @param sortOrder  the sort order (asc or desc)
     * @param startDate  the start date of the flight
     * @param endDate    the end date of the flight
     * @param departure  the departure city of the flight
     * @param destination the destination city of the flight
     * @param minDuration the minimum duration of the flight
     * @param maxDuration the maximum duration of the flight
     * @param minPrice    the minimum price of the flight
     * @param maxPrice    the maximum price of the flight
     * @return a page of flights
     */
    @CrossOrigin
    @GetMapping("/search")
    public ResponseEntity<Page<Flight>> findFlights(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departureTime") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Pageable pageable;

        // Determine the sort order for the pageable object
        if (sortOrder.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        }

        // Get the page of flights based on the search criteria
        Page<Flight> flightsPage = flightService.findFlights(startDate, endDate, departure, destination,
                minDuration, maxDuration, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(flightsPage);
    }
}
