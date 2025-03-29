package org.example.flight_booking.utils;

import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeatRecommendation {
    /**
     * Recommends a list of seats based on the given criteria.
     *
     * @param seatPlan     the seat plan to recommend seats from
     * @param numSeats     the number of seats to recommend
     * @param window       whether the seat should be a window seat
     * @param aisle        whether the seat should be an aisle seat
     * @param extraLegroom whether the seat should have extra legroom
     * @param nearExit     whether the seat should be near an exit
     * @param adjacent     whether the seats should be adjacent
     * @param selectedSeats the list of already selected seats
     * @return a list of recommended seats
     */
    public static List<Seat> recommendSeats(SeatPlan seatPlan, int numSeats,
                                            boolean window, boolean aisle, boolean extraLegroom,
                                            boolean nearExit, boolean adjacent, List<Long> selectedSeats) {
        // Remove already selected seats
        List<Seat> availableSeats = seatPlan.findAllFreeSeats().stream()
                .filter(seat -> !selectedSeats.contains(seat.getId()))
                .collect(Collectors.toList());

        // Filter seats based on criteria
        if (window) {
            availableSeats = availableSeats.stream().filter(Seat::isWindowSeat).collect(Collectors.toList());
        }
        if (aisle) {
            availableSeats = availableSeats.stream().filter(Seat::isAisleSeat).collect(Collectors.toList());
        }
        if (extraLegroom) {
            availableSeats = availableSeats.stream().filter(Seat::isExtraLegroom).collect(Collectors.toList());
        }
        if (nearExit) {
            availableSeats = availableSeats.stream().filter(Seat::isNearExit).collect(Collectors.toList());
        }

        // If adjacent seats are requested, find available adjacent seats
        if (adjacent && numSeats > 1) {
            availableSeats = findAdjacentSeats(availableSeats, numSeats);
        }

        // If there are not enough seats available, return an empty list
        if (availableSeats.size() < numSeats) {
            return Collections.emptyList();
        }

        return availableSeats.subList(0, numSeats);
    }

    private static List<Seat> findAdjacentSeats(List<Seat> availableSeats, int numSeats) {
        return Collections.emptyList();
    }
}
