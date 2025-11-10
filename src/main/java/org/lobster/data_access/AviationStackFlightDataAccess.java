// File: src/main/java/org/lobster/data_access/AviationStackFlightDataAccess.java
package org.lobster.data_access;

import org.lobster.entity.*;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.List;
import java.util.ArrayList;

public class AviationStackFlightDataAccess implements FlightDataAccessInterface {

    private final String API_ACCESS_KEY = "9c01eedb6f752b68d0e0acb28623c00c"; // Replace with your actual key
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AviationStackFlightDataAccess() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        try {
            String url = "http://api.aviationstack.com/v1/flights?access_key=" + API_ACCESS_KEY +
                    "&flight_iata=" + flightNumber + "&limit=1";

            System.out.println("=== AVIATIONSTACK API DEBUG ===");
            System.out.println("Request URL: " + url.replace(API_ACCESS_KEY, "REDACTED"));

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Message: " + response.message());

                if (!response.isSuccessful()) {
                    System.err.println("API request failed: " + response.code());
                    return createFallbackFlight(flightNumber);
                }

                String responseBody = response.body().string();

                // Log the complete raw response
                System.out.println("=== RAW API RESPONSE ===");
                System.out.println(responseBody);
                System.out.println("=== END RAW RESPONSE ===");

                // Try to parse the response
                AviationStackResponse apiResponse = objectMapper.readValue(responseBody, AviationStackResponse.class);

                if (apiResponse.data != null && apiResponse.data.length > 0) {
                    System.out.println("Found " + apiResponse.data.length + " flight(s) in response");
                    AviationStackResponse.FlightData flightData = apiResponse.data[0];

                    // Debug the flight data structure
                    System.out.println("Flight Data Structure:");
                    System.out.println("  - Flight IATA: " + (flightData.flight != null ? flightData.flight.iata : "null"));
                    System.out.println("  - Airline: " + (flightData.airline != null ? flightData.airline.name : "null"));
                    System.out.println("  - Departure: " + (flightData.departure != null ? flightData.departure.iata : "null"));
                    System.out.println("  - Arrival: " + (flightData.arrival != null ? flightData.arrival.iata : "null"));
                    System.out.println("  - Status: " + flightData.flight_status);

                    return mapToDomainFlight(flightData);
                } else {
                    System.out.println("No flight data found in response array");
                    if (apiResponse.data == null) {
                        System.out.println("Data array is null");
                    } else {
                        System.out.println("Data array is empty");
                    }
                    return createFallbackFlight(flightNumber);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching flight data: " + e.getMessage());
            e.printStackTrace();
            return createFallbackFlight(flightNumber);
        }
    }

    private Flight mapToDomainFlight(AviationStackResponse.FlightData apiFlight) {
        // Extract data from the API response
        String flightIata = apiFlight.flight != null ? apiFlight.flight.iata : "UNKNOWN";
        String airlineName = apiFlight.airline != null ? apiFlight.airline.name : "Unknown Airline";

        // FIX: Call mapFlightStatus which returns FlightStatus enum, not String
        FlightStatus status = mapFlightStatus(apiFlight.flight_status);

        // Create Airport objects for departure and arrival
        Airport departure = createAirport(apiFlight.departure);
        Airport arrival = createAirport(apiFlight.arrival);

        // Create LivePosition if live data is available
        LivePosition livePos = null;
        if (apiFlight.live != null && !apiFlight.live.is_ground &&
                apiFlight.live.latitude != 0.0 && apiFlight.live.longitude != 0.0) {
            livePos = new LivePosition(
                    apiFlight.live.latitude,
                    apiFlight.live.longitude,
                    (int) (apiFlight.live.altitude * 3.28084), // Convert meters to feet
                    apiFlight.live.speed_horizontal * 1.94384, // Convert m/s to knots
                    apiFlight.live.direction
            );
        }

        return new Flight(flightIata, airlineName, departure, arrival, status, livePos);
    }

    private Airport createAirport(AviationStackResponse.AirportInfo apiAirport) {
        if (apiAirport == null) {
            return new Airport("UNK", "UNKN", "Unknown Airport", "Unknown", "Unknown");
        }

        String airportName = apiAirport.airport != null ? apiAirport.airport : "Unknown Airport";
        String iata = apiAirport.iata != null ? apiAirport.iata : "UNK";
        String icao = apiAirport.icao != null ? apiAirport.icao : "UNKN";

        return new Airport(
                iata,
                icao,
                airportName,
                "Unknown", // City - would need additional mapping
                "Unknown"  // Country - would need additional mapping
        );
    }

    // FIX: Return FlightStatus enum instead of String
    private FlightStatus mapFlightStatus(String apiStatus) {
        if (apiStatus == null) {
            return FlightStatus.SCHEDULED;
        }

        switch (apiStatus.toLowerCase()) {
            case "active": return FlightStatus.IN_AIR;
            case "scheduled": return FlightStatus.SCHEDULED;
            case "landed": return FlightStatus.LANDED;
            case "cancelled": return FlightStatus.CANCELLED;
            case "incident":
            case "diverted":
            case "delayed": return FlightStatus.DELAYED;
            default: return FlightStatus.SCHEDULED;
        }
    }

    // FIX: Create a proper fallback flight without relying on MockFlightDataAccess
    private Flight createFallbackFlight(String flightNumber) {
        // Create a simple fallback flight directly
        String airlineName = getAirlineNameFromFlightNumber(flightNumber);
        Airport unknownDeparture = new Airport("UNK", "UNKN", "Unknown Departure", "Unknown", "Unknown");
        Airport unknownArrival = new Airport("UNK", "UNKN", "Unknown Arrival", "Unknown", "Unknown");

        return new Flight(flightNumber, airlineName, unknownDeparture, unknownArrival,
                FlightStatus.SCHEDULED, null);
    }

    // Add the missing method that was in MockFlightDataAccess
    private String getAirlineNameFromFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.length() < 2) {
            return "Unknown Airline";
        }

        String airlineCode = flightNumber.substring(0, 2).toUpperCase();

        switch (airlineCode) {
            case "AC": return "Air Canada";
            case "DL": return "Delta Airlines";
            case "UA": return "United Airlines";
            case "AA": return "American Airlines";
            case "BA": return "British Airways";
            case "LH": return "Lufthansa";
            case "AF": return "Air France";
            case "EK": return "Emirates";
            case "SQ": return "Singapore Airlines";
            case "JL": return "Japan Airlines";
            case "CX": return "Cathay Pacific";
            case "WN": return "Southwest Airlines";
            case "NK": return "Spirit Airlines";
            case "B6": return "JetBlue Airways";
            case "AS": return "Alaska Airlines";
            case "F9": return "Frontier Airlines";
            case "HA": return "Hawaiian Airlines";
            case "SY": return "Sun Country Airlines";
            default:
                // Try 3-character codes
                if (flightNumber.length() >= 3) {
                    String airlineCode3 = flightNumber.substring(0, 3).toUpperCase();
                    switch (airlineCode3) {
                        case "UAL": return "United Airlines";
                        case "DAL": return "Delta Airlines";
                        case "AAL": return "American Airlines";
                        case "ACA": return "Air Canada";
                        case "BAW": return "British Airways";
                        case "AFR": return "Air France";
                        case "EIN": return "Aer Lingus";
                        default: return "Unknown Airline";
                    }
                }
                return "Unknown Airline";
        }
    }

    // Inner classes to model the AviationStack API JSON response
    public static class AviationStackResponse {
        public Pagination pagination;
        public FlightData[] data;

        public static class Pagination {
            public int limit;
            public int offset;
            public int count;
            public int total;
        }

        public static class FlightData {
            public String flight_date;
            public String flight_status;
            public AirportInfo departure;
            public AirportInfo arrival;
            public Airline airline;
            public Flight flight;
            public Live live;
        }

        public static class AirportInfo {
            public String airport;
            public String iata;
            public String icao;
            public String terminal;
            public String gate;
            public Integer delay;
        }

        public static class Airline {
            public String name;
            public String iata;
            public String icao;
        }

        public static class Flight {
            public String number;
            public String iata;
            public String icao;
        }

        public static class Live {
            public String updated;
            public double latitude;
            public double longitude;
            public double altitude;
            public double direction;
            public double speed_horizontal;
            public double speed_vertical;
            public boolean is_ground;
        }
    }
}