package org.lobster.use_case.search_flight;

import org.lobster.entity.Flight;
public class SearchFlightOutputData {
    private final Flight flight;
    private final String message;

    public SearchFlightOutputData(Flight flight, String message) {
        this.flight = flight;
        this.message = message;
    }
    public Flight getFlight() {
        return flight;
    }
    public String getMessage() {
        return message;
    }
}
