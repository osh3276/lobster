package org.lobster.use_case.browse_airport;

import org.lobster.entity.Airport;

public class BrowseAirportOutputData {

    private final Airport airport;
    private final String message;

    public BrowseAirportOutputData(Airport airport, String message) {
        this.airport = airport;
        this.message = message;
    }

    public Airport getAirport() { return airport; }
    public String getMessage() { return message; }
}