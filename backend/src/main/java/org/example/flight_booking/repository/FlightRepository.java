package org.example.flight_booking.repository;

import org.example.flight_booking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureIATA(String departureIATA);
    Flight findById(long id);
}
