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
        StringBuilder sb = new StringBuilder();

        sb.append("Flight ")
                .append(flightNumber != null ? flightNumber : "Unknown");

        if (airline != null) {
            sb.append(" (").append(airline.getName()).append(")");
        }

        if (callsign != null && !callsign.isEmpty()) {
            sb.append(" [").append(callsign).append("]");
        }
        sb.append('\n');

        sb.append("Route: ")
                .append(departure != null ? departure.getIata() : "???")
                .append(" → ")
                .append(arrival != null ? arrival.getIata() : "???")
                .append('\n');

        if (eta != null) {
            sb.append("ETA: ").append(eta).append('\n');
        }

        if (status != null) {
            sb.append("Status: ").append(status).append('\n');
        }

        if (livePosition != null) {
            sb.append(String.format(
                    "Position: %.5f°, %.5f° | Alt: %,d ft | Speed: %.0f kt | Heading: %.0f°",
                    livePosition.getLatitude(),
                    livePosition.getLongitude(),
                    livePosition.getAltitude(),
                    livePosition.getGroundSpeed(),
                    livePosition.getTrack()
            ));
        } else {
            sb.append("Position: N/A");
        }

        return sb.toString();
    }
}