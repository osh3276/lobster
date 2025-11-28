package org.lobster.mocks;

import org.lobster.entity.Flight;
import org.lobster.entity.Airline;
import org.lobster.entity.Airport;
import org.lobster.interface_adapter.FlightDataAccessInterface;

/**
 * Mock implementation of FlightDataAccessInterface for unit testing
 */
public class MockFlightDataGateway implements FlightDataAccessInterface {
    private Flight flightToReturn = null;
    private boolean throwException = false;
    private String lastFlightNumberQueried = null;
    private String lastCallSignQueried = null;

    public void setFlightToReturn(Flight flight) {
        this.flightToReturn = flight;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public String getLastFlightNumberQueried() {
        return lastFlightNumberQueried;
    }

    public String getLastCallSignQueried() {
        return lastCallSignQueried;
    }

    public void reset() {
        this.flightToReturn = null;
        this.throwException = false;
        this.lastFlightNumberQueried = null;
        this.lastCallSignQueried = null;
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        if (throwException) {
            throw new RuntimeException("Mock database error in flight lookup");
        }
        this.lastFlightNumberQueried = flightNumber;
        return flightToReturn;
    }

    @Override
    public Flight findByCallSign(String callSign) {
        if (throwException) {
            throw new RuntimeException("Mock database error in callsign lookup");
        }
        this.lastCallSignQueried = callSign;
        return flightToReturn;
    }

    @Override
    public Airport findAirportByIcao(String icao) {
        // Return a simple mock airport for testing
        return new Airport("LAX", "KLAX", "Los Angeles International Airport");
    }

    @Override
    public Airline findAirlineByIata(String iata) {
        // Return a simple mock airline for testing
        return new Airline("AA", "AAL", "American Airlines");
    }
}