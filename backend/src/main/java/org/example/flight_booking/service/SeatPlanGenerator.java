package org.example.flight_booking.service;

import org.example.flight_booking.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SeatPlanGenerator {
    private static final Logger log = LoggerFactory.getLogger(SeatPlanGenerator.class);
    private final Random random = new Random();

    @Value("#{${seatPlanFileMap}}")
    private Map<String, String> seatPlanFileMap;

    public void generateSeatPlan(Flight flight){
        if (seatPlanFileMap.containsKey(flight.getAircraftType())) {
            flight.setSeatPlan(generateSeatPlanFromFile(seatPlanFileMap.get(flight.getAircraftType()), flight.getId()));
        } else {
            flight.setSeatPlan(generateRandomSeatPlan(flight.getId()));
        }
    }

    private SeatPlan generateRandomSeatPlan(long flightId){
        List<String> aircraftTypes = seatPlanFileMap.keySet().stream().toList();
        String randomType = aircraftTypes.get(random.nextInt(aircraftTypes.size()));
        return generateSeatPlanFromFile(seatPlanFileMap.get(randomType), flightId);
    }

    private SeatPlan generateSeatPlanFromFile(String filePath, long flightId){
        List<SeatRow> rows = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                SeatRow row = createRow(parts[0], parts[1].toCharArray(), parts.length > 2, flightId);
                rows.add(row);
            }
        } catch (Exception e) {
            log.error("Error reading seat plan file: {}", e.getMessage());
            return null;
        }

        return new SeatPlan(rows);
    }

    private SeatRow createRow(String rowNumber, char[] seatLayout, boolean nearExit, long flightId) {
        List<Seat> seats = new ArrayList<>();
        List<Integer> aisles = new ArrayList<>();

        // Create seats
        for (int i = 0; i < seatLayout.length; i++) {
            char seatType = seatLayout[i];
            if (seatType == '_') {
                seats.add(new NoSeat(flightId));
            } else if (seatType == '|' ) {
                seats.add(new NoSeat(flightId));
                aisles.add(i);
            } else {
                String seatNumber = rowNumber + Character.toUpperCase(seatType);
                boolean hasExtraLegroom = Character.isUpperCase(seatType);

                seats.add(new Seat(flightId, seatNumber, false, false, hasExtraLegroom, nearExit));
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
