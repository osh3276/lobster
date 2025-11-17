package org.lobster.use_case.browse_airport;

public class BrowseAirportInputData {
    private final String airportCode;
    private final String type;

    public BrowseAirportInputData(String airportCode, String type) {
        this.airportCode = airportCode.toUpperCase().trim();
        this.type = type.toLowerCase().trim();
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getType() {
        return type;
    }
}
