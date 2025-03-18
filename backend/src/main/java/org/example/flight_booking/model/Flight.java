package org.example.flight_booking.model;


import java.time.LocalDateTime;

public class Flight {
    private long id;

    private String flightNumber;
    private String fromIATA;
    private String toIATA;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private SeatPlan seatPlan;

    public Flight(String flightNumber, String fromIATA, String toIATA, LocalDateTime departureTime, LocalDateTime arrivalTime, SeatPlan seatPlan) {
        this.flightNumber = flightNumber;
        this.fromIATA = fromIATA;
        this.toIATA = toIATA;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatPlan = seatPlan;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", fromIATA='" + fromIATA + '\'' +
                ", toIATA='" + toIATA + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", seatPlan=" + seatPlan +
                '}';
    }
}
