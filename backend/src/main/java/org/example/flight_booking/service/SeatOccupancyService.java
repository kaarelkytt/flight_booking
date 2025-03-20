package org.example.flight_booking.service;

import org.example.flight_booking.model.Flight;
import org.example.flight_booking.model.Seat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SeatOccupancyService {
    private final Random random = new Random();

    @Value("${flight.occupancy.min}")
    private double minOccupancy;

    @Value("${flight.occupancy.max}")
    private double maxOccupancy;

    @Value("${flight.occupancy.threshold.days}")
    private int occupancyThresholdDays;

    public void fillSeatsRandomly(Flight flight) {
        double fillPercentage = calculateFillPercentage(flight.getDepartureTime().toLocalDate());
        List<Seat> allAvailableSeats = flight.getSeatPlan().getAllSeats();

        int seatsToFill = (int) (allAvailableSeats.size() * fillPercentage);
        for (int i = 0; i < seatsToFill; i++) {
            int randomIndex = random.nextInt(allAvailableSeats.size());
            allAvailableSeats.get(randomIndex).setOccupied(true);
            allAvailableSeats.remove(randomIndex);
        }
    }

    private double calculateFillPercentage(LocalDate flightDate) {
        int daysUntilFlight = (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), flightDate);

        if (daysUntilFlight >= occupancyThresholdDays) {
            return minOccupancy;
        } else {
            double ratio = (double) daysUntilFlight / occupancyThresholdDays;
            return minOccupancy + (maxOccupancy - minOccupancy) * (1 - ratio);
        }
    }
}
