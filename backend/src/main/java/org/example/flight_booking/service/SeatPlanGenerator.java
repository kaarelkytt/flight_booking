package org.example.flight_booking.service;

import org.example.flight_booking.model.NoSeat;
import org.example.flight_booking.model.Seat;
import org.example.flight_booking.model.SeatPlan;
import org.example.flight_booking.model.SeatRow;

import java.util.*;

public class SeatPlanGenerator {
    private static final Random random = new Random();

    public static SeatPlan generateSeatPlan(String aircraftType){
        return switch (aircraftType) {
            case "BCS3" -> generateA220SeatPlan();
            case "AT75" -> generateAT75SeatPlan();
            case "A20N", "A320", "A321" -> generateA320SeatPlan();
            case "B738", "B38M" -> generateB737SeatPlan();
            case "B748" -> generateB747SeatPlan();
            default -> generateRandomSeatPlan();
        };
    }

    private static SeatPlan generateA220SeatPlan() {
        /* Airbus A220-300 plan
        1  ___D_F ex
        2  A__D_F ex(A)
        3  AC_DEF
        ...
        13 AC_DEF ex
        14 AC_DEF ex
        ...
        31 AC_DEF
         */

        return null;
    }

    private static SeatPlan generateAT75SeatPlan() {
        /* ATR 72-500 plan
        1  AB_CD ex
        17 AB_CD
        ...
        18 AB___
         */

        return null;
    }

    private static SeatPlan generateA320SeatPlan() {
        /* Airbus A320 Neo plan
        1  ABC_DEF
        2  abc_def
        ...
        11 ABC_DEF
        12 ABC_DEF
        ...
        29 abc_def
         */

        return null;
    }

    private static SeatPlan generateB737SeatPlan() {
        /* Boing 737-800 plan
        1  ___|DEF
        2  ABC|def
        ...
        15 ABC|DEF
        16 _BC|DE_
        17 abc|def
        ...
        32 abc|def
         */

        return null;
    }

    private static SeatPlan generateB747SeatPlan() {
        /* Boing 747-8 plan
        1  _BC|DEFG|HJ_ ex
        ...
        3  _BC|DEFG|HJ_
        4  ABC|DEFG|HJK ex(AK)
        ...
        15 ABC|DEFG|HJK ex
        16 ABC|DEFG|HJK ex
        ...
        27 ABC|DEFG|HJK ex
        ...
        44 ABC|DEFG|HJK
        45 _BC|DEFG|HJ_
        ...
        47 _BC|DEFG|HJ_
        48 ___|DEFG|___
        49 ___|DEFG|___
         */

        return null;
    }

    public static SeatPlan generateRandomSeatPlan(){
        List<String> aircraftTypes = Arrays.asList("BCS3", "AT75", "A20N", "B738", "B748");
        String randomType = aircraftTypes.get(random.nextInt(aircraftTypes.size()));
        return generateSeatPlan(randomType);
    }

    private static SeatRow generateRow(int rowNumber, char[] seatLayout) {
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
                String seatNumber = rowNumber + Character.toString(Character.toUpperCase(seatType));
                boolean hasExtraLegroom = Character.isUpperCase(seatType);

                seats.add(new Seat(seatNumber, false, false, hasExtraLegroom, false));
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

    public static void main(String[] args) {
        SeatPlan seatPlan = generateRandomSeatPlanTest();
        System.out.println(seatPlan);
    }
}
