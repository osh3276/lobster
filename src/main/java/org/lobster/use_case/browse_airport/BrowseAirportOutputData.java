package org.lobster.use_case.browse_airport;

import org.lobster.entity.Flight;
import java.util.List;

public class BrowseAirportOutputData {
    private final List<Flight> flights;
    private final String message;

    public BrowseAirportOutputData(List<Flight> flights, String message) {
        this.flights = flights;
        this.message = message;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getMessage() {
        return message;
    }
}
