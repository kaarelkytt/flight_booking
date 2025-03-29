package org.example.flight_booking.utils;

import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;
import org.example.flight_booking.model.SeatRow;

import java.util.*;
import java.util.stream.Collectors;

public class SeatRecommendation {

    /**
     * Recommends a list of seats based on the given criteria.
     *
     * @param seatPlan      the seat plan to recommend seats from
     * @param numSeats      the number of seats to recommend
     * @param window        whether the seat should be a window seat
     * @param aisle         whether the seat should be an aisle seat
     * @param extraLegroom  whether the seat should have extra legroom
     * @param nearExit      whether the seat should be near an exit
     * @param adjacent      whether the seats should be adjacent
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

        // If there are not enough seats available, return an empty list
        if (availableSeats.size() < numSeats) {
            return Collections.emptyList();
        }


        if (adjacent) {
            return findSuitableAdjacentSeats(seatPlan, availableSeats, numSeats, window, aisle, extraLegroom, nearExit);
        } else {
            return findSuitableSeats(availableSeats, numSeats, window, aisle, extraLegroom, nearExit);
        }
    }

    /**
     * Find one suitable seat based on the given criteria.
     *
     * @param availableSeats the list of available seats
     * @param window         whether the seat should be a window seat
     * @param aisle          whether the seat should be an aisle seat
     * @param extraLegroom   whether the seat should have extra legroom
     * @param nearExit       whether the seat should be near an exit
     * @return recommended seats
     */
    private static List<Seat> findSuitableSeats(List<Seat> availableSeats, int numSeats,
                                                boolean window, boolean aisle,
                                                boolean extraLegroom, boolean nearExit) {

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

        // If there are not enough seats available, return an empty list
        if (availableSeats.size() < numSeats) {
            return Collections.emptyList();
        }

        // return random seats from available seats
        Collections.shuffle(availableSeats);
        return availableSeats.subList(0, numSeats);
    }

    /**
     * Finds the best group of adjacent seats that match the given preferences.
     * It scans through the seat plan row by row, considering only available seats.
     * The groups are evaluated based on a scoring system, and the highest-scoring group is selected.
     *
     * @param seatPlan       the seat plan to recommend seats from
     * @param availableSeats the list of available seats
     * @param numSeats       the number of seats needed
     * @param window         whether the seat should be a window seat
     * @param aisle          whether the seat should be an aisle seat
     * @param extraLegroom   whether the seat should have extra legroom
     * @param nearExit       whether the seat should be near an exit
     * @return a list of adjacent seats
     */
    private static List<Seat> findSuitableAdjacentSeats(SeatPlan seatPlan, List<Seat> availableSeats,
                                                        int numSeats,
                                                        boolean window, boolean aisle,
                                                        boolean extraLegroom, boolean nearExit) {

        List<List<Seat>> suitableAdjacentSeats = new ArrayList<>();
        int maxScore = -1;

        List<Seat> currentGroup = new ArrayList<>();
        List<Integer> gaps = new ArrayList<>();
        int near = 4;

        for (SeatRow row : seatPlan.getSeatRows()) {
            for (Seat seat : row.getSeats()) {
                // If it's a NoSeat (aisle), decrease the 'near' proximity score and continue
                if (seat.getSeatNumber() == null) {
                    near -= 1;
                    continue;
                }

                // If seat is not available, reset current group and gaps
                if (!availableSeats.contains(seat)) {
                    currentGroup.clear();
                    gaps.clear();
                    near = 4;
                    continue;
                }

                // Track gap scores between seats
                if (!currentGroup.isEmpty()) {
                    gaps.add(near);
                    near = 4;
                }

                currentGroup.add(seat);

                // Maintain a sliding window of size numSeats
                if (currentGroup.size() > numSeats) {
                    currentGroup.removeFirst();
                    gaps.removeFirst();
                }

                // Evaluate the current group if it contains the required number of seats
                if (currentGroup.size() == numSeats) {
                    int score = calculateScore(currentGroup, gaps, window, aisle, extraLegroom, nearExit);

                    if (score > maxScore) {
                        maxScore = score;
                        suitableAdjacentSeats.clear();
                        suitableAdjacentSeats.add(new ArrayList<>(currentGroup));
                    } else if (score == maxScore) {
                        suitableAdjacentSeats.add(new ArrayList<>(currentGroup));
                    }
                }
            }
            // Reduce the proximity score for the next row
            near -= 2;
        }

        // If no suitable seats found, return an empty list; otherwise, return a randomly chosen best group
        if (suitableAdjacentSeats.isEmpty()) {
            return Collections.emptyList();
        } else {
            Collections.shuffle(suitableAdjacentSeats);
            return suitableAdjacentSeats.getFirst();
        }
    }

    /**
     * Calculates the score for a given set of adjacent seats based on passenger preferences.
     * The score is computed based on gaps between seats (ensuring proximity) and how well
     * each seat matches the requested attributes (window, aisle, extra legroom, near exit).
     *
     * @param seats        The list of seats being evaluated.
     * @param gaps         The list of gaps between the seats in the group.
     * @param window       Whether a window seat is preferred.
     * @param aisle        Whether an aisle seat is preferred.
     * @param extraLegroom Whether extra legroom is preferred.
     * @param nearExit     Whether proximity to an exit is preferred.
     * @return A numerical score indicating how well the seats match the preferences.
     */
    private static int calculateScore(List<Seat> seats, List<Integer> gaps,
                                      boolean window, boolean aisle,
                                      boolean extraLegroom, boolean nearExit) {
        // Sum of gaps, ensuring closer seats are prioritized
        int score = gaps.stream().mapToInt(gap -> gap).sum();

        // Add score based on each seat's attributes matching user preferences
        for (Seat seat : seats) {
            if (window == seat.isWindowSeat()) score++;
            if (aisle == seat.isAisleSeat()) score++;
            if (extraLegroom == seat.isExtraLegroom()) score++;
            if (nearExit == seat.isNearExit()) score++;
        }

        // Additional scoring to prioritize the requested attributes even more
        for (Seat seat : seats) {
            if (window && seat.isWindowSeat()) score++;
            if (aisle && seat.isAisleSeat()) score++;
            if (extraLegroom && seat.isExtraLegroom()) score++;
            if (nearExit && seat.isNearExit()) score++;
        }

        return score;
    }
}
