package org.example.flight_booking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FlightPricingService {
    private final Random random = new Random();

    @Value("${flight.price.base}")
    private double basePrice;

    @Value("${flight.price.minute}")
    private double pricePerMinute;

    @Value("${flight.price.variation}")
    private double variation;

    public double calculateInitialPrice(int durationMinutes) {
        double randomFactor = 1 + (random.nextDouble() * variation * 2 - variation);
        double initialPrice = (basePrice + durationMinutes * pricePerMinute) * randomFactor;
        return Math.round(initialPrice);
    }
}
