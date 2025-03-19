package org.example.flight_booking.model;


import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String flightNumber;
    private String departureIATA;
    private String destinationIATA;
    private String departureCity;
    private String destinationCity;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
    private String aircraftType;

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private SeatPlan seatPlan;

    public Flight(String flightNumber, String departureIATA, String destinationIATA, String departureCity, String destinationCity, OffsetDateTime departureTime, OffsetDateTime arrivalTime, String aircraftType) {
        this.flightNumber = flightNumber;
        this.departureIATA = departureIATA;
        this.destinationIATA = destinationIATA;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftType = aircraftType;
    }

    public Flight() {
    }

    public long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartureIATA() {
        return departureIATA;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public OffsetDateTime getArrivalTime() {
        return arrivalTime;
    }

    public SeatPlan getSeatPlan() {
        return seatPlan;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setSeatPlan(SeatPlan seatPlan) {
        seatPlan.setFlight(this);
        this.seatPlan = seatPlan;
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
