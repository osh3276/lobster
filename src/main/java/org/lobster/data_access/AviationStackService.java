package org.lobster.data_access;

import org.lobster.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AviationStackService {

    private final String API_ACCESS_KEY = "9c01eedb6f752b68d0e0acb28623c00c"; // Replace with your key
    private final String BASE_URL = "http://api.aviationstack.com/v1/";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Flight findFlightByNumber(String flightNumber) throws Exception {
        // Construct the API URL with parameters
        String url = String.format(
                "%sflights?access_key=%s&flight_iata=%s&limit=1",
                BASE_URL, API_ACCESS_KEY, flightNumber
        );

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("API request failed: " + response.code() + " - " + response.message());
            }

            String responseBody = response.body().string();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");

            if (dataNode.isArray() && dataNode.size() > 0) {
                // Get the first flight from the response array
                JsonNode flightData = dataNode.get(0);
                return mapApiResponseToFlight(flightData);
            } else {
                throw new RuntimeException("No flight data found for: " + flightNumber);
            }
        }
    }

    private Flight mapApiResponseToFlight(JsonNode flightData) {
        // 1. Extract basic flight information
        String apiFlightNumber = flightData.path("flight").path("iata").asText();
        String airlineName = flightData.path("airline").path("name").asText("Unknown Airline");

        // 2. Map Flight Status
        String apiStatus = flightData.path("flight_status").asText("unknown");
        FlightStatus status = mapFlightStatus(apiStatus);

        // 3. Create Departure and Arrival Airport objects [citation:3][citation:5]
        JsonNode dep = flightData.path("departure");
        JsonNode arr = flightData.path("arrival");

        Airport departure = new Airport(
                dep.path("iata").asText("UNK"),
                dep.path("icao").asText("UNKN"),
                dep.path("airport").asText("Unknown Departure"),
                dep.path("timezone").asText("Unknown").split("/")[0], // Extract city from timezone as a simple fallback
                "Unknown" // Country would need a more robust mapping
        );

        Airport arrival = new Airport(
                arr.path("iata").asText("UNK"),
                arr.path("icao").asText("UNKN"),
                arr.path("airport").asText("Unknown Arrival"),
                arr.path("timezone").asText("Unknown").split("/")[0],
                "Unknown"
        );

        // 4. Create LivePosition if available [citation:3][citation:5]
        LivePosition livePos = null;
        JsonNode live = flightData.path("live");

        if (!live.isMissingNode() && !live.path("is_ground").asBoolean()) {
            livePos = new LivePosition(
                    live.path("latitude").asDouble(),
                    live.path("longitude").asDouble(),
                    (int) (live.path("altitude").asDouble() * 3.28084), // Convert meters to feet
                    live.path("speed_horizontal").asDouble() * 1.94384, // Convert m/s to knots
                    live.path("direction").asDouble()
            );
        }

        // 5. Create and return the Flight entity
        return new Flight(apiFlightNumber, airlineName, departure, arrival, status, livePos);
    }

    private FlightStatus mapFlightStatus(String apiStatus) {
        switch (apiStatus.toLowerCase()) {
            case "scheduled":
                return FlightStatus.SCHEDULED;
            case "active":
                return FlightStatus.IN_AIR;
            case "landed":
                return FlightStatus.LANDED;
            case "cancelled":
                return FlightStatus.CANCELLED; // You might need to add this to your FlightStatus enum
            case "diverted":
            case "incident":
                return FlightStatus.DELAYED; // Or create a new status for these
            default:
                return FlightStatus.SCHEDULED;
        }
    }
}