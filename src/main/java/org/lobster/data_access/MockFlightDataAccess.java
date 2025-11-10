package org.lobster.data_access;

import org.lobster.entity.*;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.interface_adapter.AirportDataAccessInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class MockFlightDataAccess implements FlightDataAccessInterface, AirportDataAccessInterface {
    private final Map<String, Flight> mockFlights;
    private final List<Flight> mockDepartures;
    private final List<Flight> mockArrivals;

    public MockFlightDataAccess() {
        this.mockFlights = new HashMap<>();
        this.mockDepartures = new ArrayList<>();
        this.mockArrivals = new ArrayList<>();
        initializeMockData();
    }

    private void initializeMockData() {
        Airport yyz = new Airport("YYZ", "CYYZ", "Toronto Pearson", "Toronto", "Canada");
        Airport jfk = new Airport("JFK", "KJFK", "John F Kennedy", "New York", "USA");
        Airport lhr = new Airport("LHR", "EGLL", "Heathrow", "London", "UK");
        Airport lax = new Airport("LAX", "KLAX", "Los Angeles International", "Los Angeles", "USA");

        // Create mock flights
        Flight ac873 = new Flight("AC873", "Air Canada", yyz, lhr, FlightStatus.IN_AIR,
                new LivePosition(45.0, -60.0, 35000, 450.0, 85.0));
        Flight dl123 = new Flight("DL123", "Delta Airlines", jfk, yyz, FlightStatus.SCHEDULED, null);
        Flight ba456 = new Flight("BA456", "British Airways", lhr, jfk, FlightStatus.DELAYED, null);
        Flight ua789 = new Flight("UA789", "United Airlines", lax, yyz, FlightStatus.IN_AIR,
                new LivePosition(34.0, -118.0, 33000, 500.0, 75.0));
        Flight ac101 = new Flight("AC101", "Air Canada", yyz, jfk, FlightStatus.SCHEDULED, null);

        // Add to flight number map
        mockFlights.put("AC873", ac873);
        mockFlights.put("DL123", dl123);
        mockFlights.put("BA456", ba456);
        mockFlights.put("UA789", ua789);
        mockFlights.put("AC101", ac101);

        // Set up mock departures and arrivals by airport
        setupAirportData();
    }

    private void setupAirportData() {
        // For YYZ (Toronto Pearson) - Departures
        mockDepartures.addAll(Arrays.asList(
                mockFlights.get("AC873"), // YYZ -> LHR
                mockFlights.get("AC101")  // YYZ -> JFK
        ));

        // For YYZ (Toronto Pearson) - Arrivals
        mockArrivals.addAll(Arrays.asList(
                mockFlights.get("DL123"), // JFK -> YYZ
                mockFlights.get("UA789")  // LAX -> YYZ
        ));

        // You can add more airport data here as needed
        // For example, for JFK, LHR, LAX, etc.
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        return mockFlights.get(flightNumber.toUpperCase());
    }

    @Override
    public List<Flight> getDepartures(String airportCode) {
        // Return mock departures for the requested airport
        // For now, we'll return all mock departures for any airport code
        // You can enhance this to filter by airport code if needed
        return new ArrayList<>(mockDepartures);
    }

    @Override
    public List<Flight> getArrivals(String airportCode) {
        // Return mock arrivals for the requested airport
        // For now, we'll return all mock arrivals for any airport code
        // You can enhance this to filter by airport code if needed
        return new ArrayList<>(mockArrivals);
    }
}