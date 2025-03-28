package org.example.flight_booking.repository;

import org.example.flight_booking.model.Flight;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

    List<Flight> findByDepartureIATA(String departureIATA);

    Flight findById(long id);

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT DISTINCT f.departureIATA FROM Flight f UNION SELECT DISTINCT f.destinationIATA FROM Flight f")
    List<String> findAllUniqueAirportIataCodes();

    @Query("SELECT f.aircraftType, COUNT(f) FROM Flight f GROUP BY f.aircraftType ORDER BY COUNT(f) DESC")
    List<Object[]> findAircraftTypesWithCount();

    @Query("SELECT DISTINCT f.departureCity FROM Flight f WHERE LOWER(f.departureCity) LIKE LOWER(CONCAT(:query, '%')) ORDER BY f.departureCity ASC")
    List<String> findDepartureCities(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT f.destinationCity FROM Flight f WHERE LOWER(f.destinationCity) LIKE LOWER(CONCAT(:query, '%')) ORDER BY f.destinationCity ASC")
    List<String> findDestinationCities(@Param("query") String query, Pageable pageable);
}
