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

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public ResponseEntity<List<String>> findAllAirports() {
        return ResponseEntity.ok(flightService.findAllAirports());
    }

    @GetMapping("/aircrafts")
    public ResponseEntity<List<Object[]>> findAllAircrafts() {
        return ResponseEntity.ok(flightService.findAllAircrafts());
    }

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

        if (sortOrder.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        }

        Page<Flight> flightsPage = flightService.findFlights(startDate, endDate, departure, destination,
                minDuration, maxDuration, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(flightsPage);
    }
}
