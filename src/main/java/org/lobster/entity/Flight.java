package org.lobster.entity;

import java.util.Objects;

public class Flight {
    private final String flightNumber;
    private final String airline;
    private final Airport departure;
    private final Airport arrival;
    private final FlightStatus status;
    private final LivePosition livePosition; // Add this field

    // Updated constructor
    public Flight(String flightNumber, String airline, Airport departure, Airport arrival,
                  FlightStatus status, LivePosition livePosition) {
        this.flightNumber = Objects.requireNonNull(flightNumber);
        this.airline = Objects.requireNonNull(airline);
        this.departure = Objects.requireNonNull(departure);
        this.arrival = Objects.requireNonNull(arrival);
        this.status = Objects.requireNonNull(status);
        this.livePosition = livePosition;
    }

    // Business method to check if flight has live position
    public boolean hasLivePosition() {
        return livePosition != null && livePosition.isValid();
    }

    // Getters
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public Airport getDeparture() { return departure; }
    public Airport getArrival() { return arrival; }
    public FlightStatus getStatus() { return status; }
    public LivePosition getLivePosition() { return livePosition; }

    @Override
    public String toString() {
        return String.format("%s - %s: %s → %s", flightNumber, airline, departure.getName(), arrival.getName());
    }
}