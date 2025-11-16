package org.lobster.use_case.search_flight;

public class SearchFlightInputData {
    private final String flightNumber;

    public SearchFlightInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}
