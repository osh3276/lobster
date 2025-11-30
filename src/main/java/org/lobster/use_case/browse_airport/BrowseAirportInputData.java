package org.lobster.use_case.browse_airport;

public class BrowseAirportInputData {
    private final String airportCode;

    public BrowseAirportInputData(String airportCode) {
        this.airportCode = (airportCode == null ? "" : airportCode.trim().toUpperCase());
    }

    public String getAirportCode() {
        return airportCode;
    }
}
