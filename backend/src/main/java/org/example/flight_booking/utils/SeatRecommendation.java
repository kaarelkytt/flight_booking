package org.example.flight_booking.utils;

import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeatRecommendation {
    public static List<Seat> recommendSeats(SeatPlan seatPlan, int numSeats,
                                            boolean window, boolean aisle, boolean extraLegroom,
                                            boolean nearExit, boolean adjacent, List<Long> selectedSeats) {
        // Eemaldame juba valitud istekohad
        List<Seat> availableSeats = seatPlan.findAllFreeSeats().stream()
                .filter(seat -> !selectedSeats.contains(seat.getId()))
                .collect(Collectors.toList());

        // Rakendame eelistuste filtreid
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

        // Kui on vaja kõrvuti istekohti
        if (adjacent && numSeats > 1) {
            availableSeats = findAdjacentSeats(availableSeats, numSeats);
        }

        // Kui pole piisavalt istekohti, tagastame tühja listi
        if (availableSeats.size() < numSeats) {
            return Collections.emptyList();
        }

        return availableSeats.subList(0, numSeats);
    }

    private static List<Seat> findAdjacentSeats(List<Seat> availableSeats, int numSeats) {
        return Collections.emptyList();
    }
}
