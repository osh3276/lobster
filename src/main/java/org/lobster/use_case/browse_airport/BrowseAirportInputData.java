package org.lobster.use_case.browse_airport;

public class BrowseAirportInputData {
    private final String airportCode;
    private final String type;

    public BrowseAirportInputData(String airportCode, String type) {
        this.airportCode = (airportCode == null ? "" : airportCode.trim().toUpperCase());
        this.type = (type == null ? "" : type.trim().toLowerCase());
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getType() {
        return type;
    }
}
