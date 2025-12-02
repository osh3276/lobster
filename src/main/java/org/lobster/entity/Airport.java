
package org.lobster.entity;

public class Airport {
    private final String iata;
    private final String icao;
    private final String name;

    public Airport(String iataCode, String icaoCode, String name) {
        this.iata = iataCode;
        this.icao = icaoCode;
        this.name = name;
    }

    // Getters
    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }

    public String getName() {
        return name;
    }
}