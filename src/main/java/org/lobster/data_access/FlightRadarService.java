package org.lobster.data_access;

import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lobster.entity.Flight;

public class FlightRadarService {

    private final OkHttpClient httpClient;
    private final String API_TOKEN;

    public FlightRadarService() {
        this.httpClient = new OkHttpClient();
        this.API_TOKEN = System.getenv("API_TOKEN");
    }

    public JSONObject findByCallsign(String flightNumber) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?altitude_ranges=0-40000&callsigns=" + flightNumber;
            JSONObject flightInfo = makeRequest(url).getJSONArray("data").getJSONObject(0);
            return flightInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject findByFlightNumber(String flightNumber) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?altitude_ranges=0-40000&flight=" + flightNumber;
            JSONObject flightInfo = makeRequest(url).getJSONArray("data").getJSONObject(0);
            return flightInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // will only return the last 100 flights due to api limitations
    public JSONObject returnAllFlights() {
        try {
            String url = "/api/static/airlines/afl/light";
            return makeRequest(url);
        } catch (Exception e) {
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
        String responseBody = response.body().string();
        return new JSONObject(responseBody);
    }

    public List<Flight> getFlightsByAirport(String airportCode, String type) {
        return new ArrayList<>();
    }
}