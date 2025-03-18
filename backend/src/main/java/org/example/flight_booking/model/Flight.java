package org.example.flight_booking.model;


import jakarta.persistence.*;
import org.example.flight_booking.service.SeatPlanGenerator;

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
    private String aircraftType;

    @OneToOne
    private SeatPlan seatPlan;

    public Flight(String flightNumber, String departureIATA, String destinationIATA, LocalDateTime departureTime, LocalDateTime arrivalTime, String aircraftType) {
        this.flightNumber = flightNumber;
        this.departureIATA = departureIATA;
        this.destinationIATA = destinationIATA;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftType = aircraftType;
        SeatPlanGenerator generator = new SeatPlanGenerator(this);
        this.seatPlan = generator.generateSeatPlan();
    }

    public Flight() {
    }

    public long getId() {
        return id;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureIATA='" + departureIATA + '\'' +
                ", destinationIATA='" + destinationIATA + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", aircraftType='" + aircraftType + '\'' +
                ", seatPlan=" + seatPlan +
                '}';
    }
}
