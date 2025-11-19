package org.lobster.entity;

public class Airline {
    private final String iata;
    private final String icao;
    private final String name;

    public Airline(String iata, String icao, String name) {
        this.iata = iata;
        this.icao = icao;
        this.name = name;
    }

    public String getIata() { return iata; }
    public String getIcao() { return icao; }
    public String getName() { return name; }

    @Override
    public String toString() {
        if (name == null) return "Unknown Airline";
        if (iata != null && !iata.isEmpty()) {
            return name + " (" + iata + ")";
        }
        return name;
    }

}
