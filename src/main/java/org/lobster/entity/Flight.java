package org.lobster.entity;

import java.util.Date;

public class Flight {
    private final String hex;
    private final String flightNumber;
    private final String callsign;
    private final Airline airline;
    private final Airport departure;
    private final Airport arrival;
    private final Date eta;
    private final FlightStatus status;
    private final LivePosition livePosition;

    // Updated constructor
    public Flight(String hex, String flightNumber, String callsign, Airline airline, Airport departure, Airport arrival, Date eta,
                  FlightStatus status, LivePosition livePosition) {
        this.hex = hex;
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.callsign = callsign;
        this.departure = departure;
        this.arrival = arrival;
        this.eta = eta;
        this.status = status;
        this.livePosition = livePosition;
    }

    // Business method to check if flight has live position
    public boolean hasLivePosition() {
        return livePosition != null && livePosition.isValid();
    }

    // Getters
    public String getCallsign() { return callsign; }
    public Airline getAirline() { return airline; }
    public Airport getDeparture() { return departure; }
    public Airport getArrival() { return arrival; }
    public FlightStatus getStatus() { return status; }
    public String getHex() { return hex; }
    public String getFlightNumber() { return flightNumber; }
    public Date getEta() { return eta; }
    public LivePosition getLivePosition() { return livePosition; }

    @Override
    public String toString() {
        return String.format("%s - %s: %s → %s", callsign, airline, departure.getName(), arrival.getName());
    }
}