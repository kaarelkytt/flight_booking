package org.example.flight_booking.service;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.model.Seat;
import org.example.flight_booking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> availableSeats(Flight flight) {
        return seatRepository.findAvailableByFlight(flight);
    }
}
