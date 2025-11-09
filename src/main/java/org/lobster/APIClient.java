package org.lobster;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class APIClient implements APIInterface {
    String clientId;
    String clientSecret;
    OkHttpClient httpClient;

    APIClient(String clientId, String clientSecret, OkHttpClient httpClient) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.httpClient = httpClient;
    }

    // TODO: Implement
    @Override
    public JSONObject fetchFlightData(String flightNumber) {
        return null;
    }

    @Override
    public JSONObject fetchAirportData(String airportCode) {
        Request request = new Request.Builder()
                .url("https://opensky-network.org/api" + "/states/all")
                .build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body() == null ? null : new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
