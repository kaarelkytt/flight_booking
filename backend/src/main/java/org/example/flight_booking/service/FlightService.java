package org.example.flight_booking.service;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> findFlights(String destination) {
        return flightRepository.findByDestinationIATA(destination);
    }
}
