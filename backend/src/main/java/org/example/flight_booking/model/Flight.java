package org.example.flight_booking.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String flightNumber;
    private String departureIATA;
    private String destinationIATA;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    @OneToOne
    private SeatPlan seatPlan;

    public Flight(String flightNumber, String departureIATA, String destinationIATA, LocalDateTime departureTime, LocalDateTime arrivalTime, SeatPlan seatPlan) {
        this.flightNumber = flightNumber;
        this.departureIATA = departureIATA;
        this.destinationIATA = destinationIATA;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatPlan = seatPlan;
    }

    public Flight() {
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", fromIATA='" + departureIATA + '\'' +
                ", toIATA='" + destinationIATA + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", seatPlan=" + seatPlan +
                '}';
    }
}
