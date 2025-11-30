package org.lobster.data_access;

import org.json.JSONObject;
import org.lobster.util.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lobster.entity.Flight;

public class FlightRadarService {

    private static final String CLASS_NAME = "FlightRadar24";
    private final OkHttpClient httpClient;
    private static final String API_TOKEN = System.getenv("API_TOKEN");

    public FlightRadarService() {
        this.httpClient = new OkHttpClient();
    }

    public JSONObject findByCallsign(String callsign) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?callsigns=" + callsign;
            return makeRequest(url).getJSONArray("data").getJSONObject(0);
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Failed to find flight by callsign: " + callsign, e);
            return null;
        }
    }

    public JSONObject findByFlightNumber(String flightNumber) {
        try {
            String url = "https://fr24api.flightradar24.com/api/live/flight-positions/full?flights=" + flightNumber;
            return makeRequest(url).getJSONArray("data").getJSONObject(0);
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Failed to find flight by flight number: " + flightNumber, e);
            return null;
        }
    }

    // will only return the last 100 flights due to api limitations
    public JSONObject returnAllFlights(String airlineCode) {
        try {
            String url = "/api/static/airlines/"+ airlineCode.toLowerCase() + "/light";
            return makeRequest(url);
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Failed to get all flights", e);
            return null;
        }
    }

    public JSONObject getAirline(String airlineCode) {
        try {
            String url = "https://fr24api.flightradar24.com/api/static/airlines/"+ airlineCode.toUpperCase() +"/light/";
            return makeRequest(url);
        } catch (Exception e) {
            Logger.getInstance().warn(CLASS_NAME, "Failed to get airline: " + airlineCode, e);
        }
        return null;
    }

    public JSONObject getAirport(String airportCode) {
        try {
            String url = "https://fr24api.flightradar24.com/api/static/airports/"+ airportCode.toUpperCase() +"/light/";
            return makeRequest(url);
        } catch (Exception e) {
            Logger.getInstance().warn(CLASS_NAME, "Failed to get airport: " + airportCode, e);
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