package org.example.flight_booking.repository;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAvailableByFlight(Flight flight);
}
