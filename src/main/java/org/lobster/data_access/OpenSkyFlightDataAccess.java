package org.lobster.data_access;

import org.lobster.entity.*;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.interface_adapter.AirportDataAccessInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public class OpenSkyFlightDataAccess implements FlightDataAccessInterface, AirportDataAccessInterface {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenSkyFlightDataAccess() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Important for Java 8 date/time
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        try {
            System.out.println("DEBUG: Searching for flight: " + flightNumber);

            // OpenSky API doesn't directly support flight number search in the free tier
            // We'll use the states/all endpoint and filter by callsign
            String url = "https://opensky-network.org/api/states/all";
            String jsonResponse = makeApiCall(url);

            if (jsonResponse != null) {
                return findFlightInStates(jsonResponse, flightNumber);
            }

        } catch (Exception e) {
            System.err.println("Error searching flight " + flightNumber + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Fallback to mock data if API fails
        return createMockFlight(flightNumber);
    }

    @Override
    public List<Flight> getDepartures(String airportCode) {
        List<Flight> departures = new ArrayList<>();
        try {
            long end = System.currentTimeMillis() / 1000;
            long begin = end - (24 * 60 * 60); // Last 24 hours

            String url = String.format(
                    "https://opensky-network.org/api/flights/departure?airport=%s&begin=%d&end=%d",
                    airportCode, begin, end
            );

            String jsonResponse = makeApiCall(url);
            if (jsonResponse != null) {
                // Parse the actual API response
                OpenSkyFlight[] flights = objectMapper.readValue(jsonResponse, OpenSkyFlight[].class);
                for (OpenSkyFlight openSkyFlight : flights) {
                    Flight flight = convertOpenSkyFlightToDomain(openSkyFlight, airportCode, "departure");
                    if (flight != null) {
                        departures.add(flight);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting departures for " + airportCode + ": " + e.getMessage());
            // Fallback to mock data
            if ("YYZ".equals(airportCode)) {
                departures.add(createMockFlight("AC873"));
            } else if ("JFK".equals(airportCode)) {
                departures.add(createMockFlight("DL123"));
            }
        }
        return departures;
    }

    @Override
    public List<Flight> getArrivals(String airportCode) {
        List<Flight> arrivals = new ArrayList<>();
        try {
            long end = System.currentTimeMillis() / 1000;
            long begin = end - (24 * 60 * 60); // Last 24 hours

            String url = String.format(
                    "https://opensky-network.org/api/flights/arrival?airport=%s&begin=%d&end=%d",
                    airportCode, begin, end
            );

            String jsonResponse = makeApiCall(url);
            if (jsonResponse != null) {
                OpenSkyFlight[] flights = objectMapper.readValue(jsonResponse, OpenSkyFlight[].class);
                for (OpenSkyFlight openSkyFlight : flights) {
                    Flight flight = convertOpenSkyFlightToDomain(openSkyFlight, airportCode, "arrival");
                    if (flight != null) {
                        arrivals.add(flight);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting arrivals for " + airportCode + ": " + e.getMessage());
            // Fallback to mock data
            if ("LHR".equals(airportCode)) {
                arrivals.add(createMockFlight("AC873"));
            } else if ("YYZ".equals(airportCode)) {
                arrivals.add(createMockFlight("DL123"));
            }
        }
        return arrivals;
    }

    private Flight findFlightInStates(String jsonResponse, String flightNumber) throws Exception {
        // Parse the states response - this is complex JSON
        OpenSkyStatesResponse response = objectMapper.readValue(jsonResponse, OpenSkyStatesResponse.class);

        if (response.states != null) {
            for (Object[] state : response.states) {
                // state[1] is the callsign in OpenSky API
                if (state.length > 1 && state[1] != null) {
                    String callsign = ((String) state[1]).trim();
                    if (flightNumber.equalsIgnoreCase(callsign)) {
                        return convertStateToFlight(state, flightNumber);
                    }
                }
            }
        }
        return null;
    }

    private Flight convertStateToFlight(Object[] state, String flightNumber) {
        try {
            // Extract data from state array
            Double longitude = state.length > 5 ? (Double) state[5] : null;
            Double latitude = state.length > 6 ? (Double) state[6] : null;
            Double altitude = state.length > 7 ? (Double) state[7] : null;
            Double velocity = state.length > 9 ? (Double) state[9] : null;

            // Create LivePosition if we have coordinates
            LivePosition livePos = null;
            if (latitude != null && longitude != null) {
                // Convert meters to feet for altitude, m/s to knots for speed
                int altitudeFeet = altitude != null ? (int)(altitude * 3.28084) : 0;
                double speedKnots = velocity != null ? velocity * 1.94384 : 0.0;

                livePos = new LivePosition(latitude, longitude, altitudeFeet, speedKnots, 0.0);
            }

            // Create generic airports (OpenSky states don't include airport info)
            Airport departure = new Airport("UNKNOWN", "UNKNOWN", "Unknown Departure", "Unknown", "Unknown");
            Airport arrival = new Airport("UNKNOWN", "UNKNOWN", "Unknown Arrival", "Unknown", "Unknown");

            return new Flight(flightNumber, "Unknown Airline", departure, arrival, FlightStatus.IN_AIR, livePos);

        } catch (Exception e) {
            System.err.println("Error converting state to flight: " + e.getMessage());
            return null;
        }
    }

    private Flight convertOpenSkyFlightToDomain(OpenSkyFlight openSkyFlight, String airportCode, String type) {
        try {
            String flightNumber = openSkyFlight.callsign;
            if (flightNumber == null || flightNumber.trim().isEmpty()) {
                return null;
            }

            // Create airport based on type
            Airport airport;
            if ("departure".equals(type)) {
                airport = createAirport(openSkyFlight.estDepartureAirport != null ?
                        openSkyFlight.estDepartureAirport : airportCode);
            } else {
                airport = createAirport(openSkyFlight.estArrivalAirport != null ?
                        openSkyFlight.estArrivalAirport : airportCode);
            }

            Airport otherAirport = new Airport("UNKNOWN", "UNKNOWN", "Unknown", "Unknown", "Unknown");

            FlightStatus status = determineFlightStatus(openSkyFlight);

            return new Flight(
                    flightNumber.trim(),
                    "Unknown Airline", // OpenSky doesn't provide airline name in flights endpoint
                    "departure".equals(type) ? airport : otherAirport,
                    "arrival".equals(type) ? airport : otherAirport,
                    status,
                    null // No live position from flights endpoint
            );

        } catch (Exception e) {
            System.err.println("Error converting OpenSky flight: " + e.getMessage());
            return null;
        }
    }

    private Airport createAirport(String icao) {
        // Simple mapping of common airports
        switch (icao) {
            case "YYZ": return new Airport("YYZ", "CYYZ", "Toronto Pearson", "Toronto", "Canada");
            case "JFK": return new Airport("JFK", "KJFK", "John F Kennedy", "New York", "USA");
            case "LHR": return new Airport("LHR", "EGLL", "Heathrow", "London", "UK");
            case "LAX": return new Airport("LAX", "KLAX", "Los Angeles", "Los Angeles", "USA");
            case "ORD": return new Airport("ORD", "KORD", "O'Hare", "Chicago", "USA");
            default: return new Airport(icao, icao, "Unknown Airport", "Unknown", "Unknown");
        }
    }

    private FlightStatus determineFlightStatus(OpenSkyFlight flight) {
        // Simple status determination based on time
        long currentTime = System.currentTimeMillis() / 1000;

        if (flight.firstSeen != null && flight.firstSeen > currentTime) {
            return FlightStatus.SCHEDULED;
        } else if (flight.lastSeen != null && flight.lastSeen < currentTime) {
            return FlightStatus.LANDED;
        } else {
            return FlightStatus.IN_AIR;
        }
    }

    private Flight createMockFlight(String flightNumber) {
        // Fallback mock data
        Airport yyz = new Airport("YYZ", "CYYZ", "Toronto Pearson", "Toronto", "Canada");
        Airport jfk = new Airport("JFK", "KJFK", "John F Kennedy", "New York", "USA");
        Airport lhr = new Airport("LHR", "EGLL", "Heathrow", "London", "UK");

        switch (flightNumber) {
            case "AC873":
                return new Flight("AC873", "Air Canada", yyz, lhr, FlightStatus.IN_AIR,
                        new LivePosition(45.0, -60.0, 35000, 450.0, 85.0));
            case "DL123":
                return new Flight("DL123", "Delta Airlines", jfk, yyz, FlightStatus.SCHEDULED, null);
            case "BA456":
                return new Flight("BA456", "British Airways", lhr, jfk, FlightStatus.DELAYED, null);
            default:
                // Create a generic flight for any flight number
                return new Flight(flightNumber, "Unknown Airline", yyz, jfk, FlightStatus.SCHEDULED, null);
        }
    }

    private String makeApiCall(String url) throws Exception {
        System.out.println("DEBUG: Making API call to: " + url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "PlainPlaneTracker/1.0")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("API call failed: " + response.code() + " - " + response.message());
                return null;
            }
            String responseBody = response.body().string();
            System.out.println("DEBUG: API response received, length: " + responseBody.length());
            return responseBody;
        }
    }

    // Inner classes for JSON parsing
    public static class OpenSkyStatesResponse {
        public int time;
        public Object[][] states;
    }

    public static class OpenSkyFlight {
        public String icao24;
        public String callsign;
        public String estDepartureAirport;
        public String estArrivalAirport;
        public Long firstSeen;
        public Long lastSeen;
    }
}