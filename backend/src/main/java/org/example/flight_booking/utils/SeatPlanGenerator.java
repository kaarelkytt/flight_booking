package org.example.flight_booking.utils;

import org.example.flight_booking.model.NoSeat;
import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;
import org.example.flight_booking.model.SeatRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatPlanGenerator {
    private static final Logger log = LoggerFactory.getLogger(SeatPlanGenerator.class);

    public static SeatPlan generateFromFile(String filePath) {
        SeatPlan seatPlan = new SeatPlan();

        try (InputStream inputStream = SeatPlanGenerator.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))){

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                SeatRow row = createRow(parts[0], parts[1].toCharArray(), parts.length > 2);
                seatPlan.addSeatRow(row);
            }
        } catch (Exception e) {
            log.error("Error reading seat plan file: {}", e.getMessage());
            return seatPlan;
        }

        return seatPlan;
    }

    private static SeatRow createRow(String rowNumber, char[] seatLayout, boolean nearExit) {
        SeatRow row = new SeatRow();
        List<Integer> aisles = new ArrayList<>();

        // Create seats
        for (int i = 0; i < seatLayout.length; i++) {
            char seatType = seatLayout[i];
            if (seatType == '_') {
                row.addSeat(new NoSeat());
            } else if (seatType == '|' ) {
                row.addSeat(new NoSeat());
                aisles.add(i);
            } else {
                String seatNumber = rowNumber + Character.toUpperCase(seatType);
                boolean hasExtraLegroom = Character.isUpperCase(seatType);

                row.addSeat(new Seat(seatNumber, false, false, hasExtraLegroom, nearExit));
            }
        }

        List<Seat> seats = row.getSeats();

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

        return row;
    }
}
