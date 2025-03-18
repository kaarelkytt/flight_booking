package org.example.flight_booking.service;

import org.example.flight_booking.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SeatPlanGenerator {
    private static final Logger log = LoggerFactory.getLogger(SeatPlanGenerator.class);
    private final Random random = new Random();
    private final Flight flight;

    @Value("#{${seatPlanFileMap}}")
    private Map<String, String> seatPlanFileMap = new HashMap<>();

    public SeatPlanGenerator(Flight flight) {
        this.flight = flight;
    }

    public SeatPlan generateSeatPlan(){
        if (seatPlanFileMap.containsKey(flight.getAircraftType())) {
            return generateSeatPlanFromFile(seatPlanFileMap.get(flight.getAircraftType()));
        } else {
            return generateRandomSeatPlan();
        }
    }

    private SeatPlan generateRandomSeatPlan(){
        List<String> aircraftTypes = seatPlanFileMap.keySet().stream().toList();
        String randomType = aircraftTypes.get(random.nextInt(aircraftTypes.size()));
        return generateSeatPlanFromFile(seatPlanFileMap.get(randomType));
    }

    private SeatPlan generateSeatPlanFromFile(String filePath) {
        List<SeatRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                SeatRow row = createRow(parts[0], parts[1].toCharArray(), parts.length > 2);
                rows.add(row);
            }
        } catch (Exception e) {
            log.error("Error reading seat plan file: {}", e.getMessage());
            return null;
        }

        return new SeatPlan(rows);
    }

    private SeatRow createRow(String rowNumber, char[] seatLayout, boolean nearExit) {
        List<Seat> seats = new ArrayList<>();
        List<Integer> aisles = new ArrayList<>();

        // Create seats
        for (int i = 0; i < seatLayout.length; i++) {
            char seatType = seatLayout[i];
            if (seatType == '_') {
                seats.add(new NoSeat());
            } else if (seatType == '|' ) {
                seats.add(new NoSeat());
                aisles.add(i);
            } else {
                String seatNumber = rowNumber + Character.toUpperCase(seatType);
                boolean hasExtraLegroom = Character.isUpperCase(seatType);

                seats.add(new Seat(flight.getId(), seatNumber, false, false, hasExtraLegroom, nearExit));
            }
        }

        // Mark aisle seats
        for (Integer i : aisles) {
            if (i > 0)
                seats.get(i-1).setAisleSeat(true);
            if (i < seats.size() - 1)
                seats.get(i+1).setAisleSeat(true);
        }

        // Mark window seats
        seats.getFirst().setWindowSeat(true);
        seats.getLast().setWindowSeat(true);


        return new SeatRow(seats);
    }
}
