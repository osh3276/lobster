package org.lobster.entity;


import java.util.Objects;

public class Airport {
    private final String iataCode;
    private final String icaoCode;
    private final String name;
    private final String city;
    private final String country;

    public Airport(String iataCode, String icaoCode, String name, String city, String country) {
        this.iataCode = Objects.requireNonNull(iataCode);
        this.icaoCode = icaoCode;
        this.name = Objects.requireNonNull(name);
        this.city = city;
        this.country = country;
    }

    // Getters
    public String getIataCode() { return iataCode; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
}