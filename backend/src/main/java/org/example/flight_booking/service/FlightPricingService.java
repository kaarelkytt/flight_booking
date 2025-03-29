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

    @Value("${flight.price.extraLegroomMultiplier}")
    private double extraLegroomMultiplier;

    @Value("${flight.price.nearExitMuliplier}")
    private double nearExitMultiplier;

    public double getExtraLegroomMultiplier() {
        return extraLegroomMultiplier;
    }

    public double getNearExitMultiplier() {
        return nearExitMultiplier;
    }

    /**
     * Calculate the initial price for a flight based on the duration in minutes.
     *
     * @param durationMinutes the duration of the flight in minutes
     * @return the initial price
     */
    public double calculateInitialPrice(int durationMinutes) {
        double randomFactor = 1 + (random.nextDouble() * variation * 2 - variation);
        double initialPrice = (basePrice + durationMinutes * pricePerMinute) * randomFactor;
        return Math.round(initialPrice);
    }
}
