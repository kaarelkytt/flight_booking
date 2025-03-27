package org.example.flight_booking.model;


import jakarta.persistence.*;

import java.time.ZonedDateTime;

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
    private ZonedDateTime departureTime;
    private ZonedDateTime arrivalTime;
    private int durationMinutes;
    private String aircraftType;
    private double initialPrice;
    private double extraLegroomMultiplier;
    private double nearExitMultiplier;

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private SeatPlan seatPlan;

    public Flight(String flightNumber, String aircraftType,
                  String departureIATA, String destinationIATA,
                  String departureCity, String destinationCity,
                  ZonedDateTime departureTime, ZonedDateTime arrivalTime,
                  int durationMinutes, double initialPrice, double extraLegroomMultiplier, double nearExitMultiplier) {
        this.flightNumber = flightNumber;
        this.departureIATA = departureIATA;
        this.destinationIATA = destinationIATA;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationMinutes = durationMinutes;
        this.aircraftType = aircraftType;
        this.initialPrice = initialPrice;
        this.extraLegroomMultiplier = extraLegroomMultiplier;
        this.nearExitMultiplier = nearExitMultiplier;
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

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public SeatPlan getSeatPlan() {
        return seatPlan;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public double getInitialPrice() {
        return initialPrice;
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
