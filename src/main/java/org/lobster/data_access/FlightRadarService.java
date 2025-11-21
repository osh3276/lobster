package org.lobster.data_access;

import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class FlightRadarService {

    private final OkHttpClient httpClient;
    private static final String API_TOKEN = System.getenv("API_TOKEN");

    public FlightRadarService() {
        this.httpClient = new OkHttpClient();
    }

    public JSONObject findByCallsign(String flightNumber) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?altitude_ranges=0-40000&callsigns=" + flightNumber;
            return makeRequest(url).getJSONArray("data").getJSONObject(0);
        } catch (Exception e) {
            System.err.println("Failed to find flight by callsign: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public JSONObject findByFlightNumber(String flightNumber) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?altitude_ranges=0-40000&flight=" + flightNumber;
            return makeRequest(url).getJSONArray("data").getJSONObject(0);
        } catch (Exception e) {
            System.err.println("Failed to find flight by flight number: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // will only return the last 100 flights due to api limitations
    public JSONObject returnAllFlights(String airlineCode) {
        try {
            String url = "/api/static/airlines/"+ airlineCode.toLowerCase() + "/light";
            return makeRequest(url);
        } catch (Exception e) {
            System.err.println("Failed to get all flights: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public JSONObject getAirline(String airlineCode) {
        try {
            String url = "https://fr24api.flightradar24.com/api/static/airlines/"+ airlineCode.toUpperCase() +"/light/";
            return makeRequest(url);
        } catch (Exception e) {
            System.err.println("Failed to get airline: " + e.getMessage());
        }
        return null;
    }

    public JSONObject getAirport(String airportCode) {
        try {
            String url = "https://fr24api.flightradar24.com/api/static/airports/"+ airportCode.toUpperCase() +"/light/";
            return makeRequest(url);
        } catch (Exception e) {
            System.err.println("Failed to get airport: " + e.getMessage());
        }
        return null;
    }

    private JSONObject makeRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Version", "v1")
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .build();

        Response response = httpClient.newCall(request).execute();
        assert response.body() != null;
        String responseBody = response.body().string();
        return new JSONObject(responseBody);
    }
}