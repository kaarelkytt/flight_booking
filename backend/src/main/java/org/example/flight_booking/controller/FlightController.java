package org.example.flight_booking.controller;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Flight> flightsPage = flightService.findFlights(startDate, endDate, departure, destination,
                minDuration, maxDuration, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(flightsPage);
    }
}
