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

    /**
     * Check if a flight with the given flight number exists
     *
     * @param flightNumber the flight number
     * @return true if a flight with the given flight number exists, false otherwise
     */
    boolean existsByFlightNumber(String flightNumber);

    /**
     * Find all unique airport IATA codes
     *
     * @return a list of unique airport IATA codes
     */
    @Query("SELECT DISTINCT f.departureIATA FROM Flight f UNION SELECT DISTINCT f.destinationIATA FROM Flight f")
    List<String> findAllUniqueAirportIataCodes();

    /**
     * Find all unique aircraft types
     *
     * @return a list of unique aircraft types
     */
    @Query("SELECT f.aircraftType, COUNT(f) FROM Flight f GROUP BY f.aircraftType ORDER BY COUNT(f) DESC")
    List<Object[]> findAircraftTypesWithCount();

    /**
     * Find all unique departure cities
     *
     * @param query    the query to filter the departure cities
     * @param pageable the pageable object
     * @return a list of unique departure cities
     */
    @Query("SELECT DISTINCT f.departureCity FROM Flight f WHERE LOWER(f.departureCity) LIKE LOWER(CONCAT(:query, '%')) ORDER BY f.departureCity ASC")
    List<String> findDepartureCities(@Param("query") String query, Pageable pageable);

    /**
     * Find all unique destination cities
     *
     * @param query    the query to filter the destination cities
     * @param pageable the pageable object
     * @return a list of unique destination cities
     */
    @Query("SELECT DISTINCT f.destinationCity FROM Flight f WHERE LOWER(f.destinationCity) LIKE LOWER(CONCAT(:query, '%')) ORDER BY f.destinationCity ASC")
    List<String> findDestinationCities(@Param("query") String query, Pageable pageable);
}
