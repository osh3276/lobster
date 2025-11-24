package org.lobster.data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lobster.entity.Flight;
import org.lobster.entity.Airport;
import org.lobster.entity.Airline;
import org.lobster.entity.LivePosition;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.util.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonFileFavoriteFlightsDAO implements FavoriteFlightsDataAccessInterface {

    private static final String CLASS_NAME = "JsonFileFavoriteFlightsDAO";
    private static final String DATA_FILE = "favorites.json";
    private final Map<String, Flight> favorites = new HashMap<>();
    private final Path dataPath;

    public JsonFileFavoriteFlightsDAO() {
        this.dataPath = Paths.get(DATA_FILE);
        loadFromFile();
    }

    public JsonFileFavoriteFlightsDAO(String customDataPath) {
        this.dataPath = Paths.get(customDataPath);
        loadFromFile();
    }

    @Override
    public void save(Flight flight) {
        favorites.put(flight.getFlightNumber(), flight);
        saveToFile();
        Logger.getInstance().info(CLASS_NAME, "Saved flight to favorites: " + flight.getFlightNumber());
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return favorites.containsKey(flightNumber);
    }

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>(favorites.values());
    }

    @Override
    public void deleteByFlightNumber(String flightNumber) {
        if (favorites.remove(flightNumber) != null) {
            saveToFile();
            Logger.getInstance().info(CLASS_NAME, "Removed flight from favorites: " + flightNumber);
        } else {
            Logger.getInstance().warn(CLASS_NAME, "Attempted to remove non-existent flight: " + flightNumber);
        }
    }

    private void loadFromFile() {
        try {
            if (Files.exists(dataPath)) {
                String content = new String(Files.readAllBytes(dataPath));
                JSONArray jsonArray = new JSONArray(content);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    Flight flight = deserializeFlight(json);
                    if (flight != null) {
                        favorites.put(flight.getFlightNumber(), flight);
                    }
                }
                Logger.getInstance().info(CLASS_NAME, "Loaded " + favorites.size() + " favorites from file");
            }
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Error loading favorites from file", e);
        }
    }

    private void saveToFile() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Flight flight : favorites.values()) {
                jsonArray.put(serializeFlight(flight));
            }

            Files.write(dataPath, jsonArray.toString(2).getBytes());
            Logger.getInstance().debug(CLASS_NAME, "Saved " + favorites.size() + " favorites to file");
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Error saving favorites to file", e);
        }
    }

    private JSONObject serializeFlight(Flight flight) {
        JSONObject json = new JSONObject();
        json.put("hex", flight.getHex());
        json.put("flightNumber", flight.getFlightNumber());
        json.put("callsign", flight.getCallsign());
        json.put("eta", flight.getEta() != null ? flight.getEta().getTime() : null);

        // Serialize nested objects if needed
        if (flight.getAirline() != null) {
            JSONObject airlineJson = new JSONObject();
            airlineJson.put("name", flight.getAirline().getName());
            airlineJson.put("iata", flight.getAirline().getIata());
            airlineJson.put("icao", flight.getAirline().getIcao());
            json.put("airline", airlineJson);
        }

        if (flight.getDeparture() != null) {
            JSONObject departureJson = new JSONObject();
            departureJson.put("name", flight.getDeparture().getName());
            departureJson.put("iata", flight.getDeparture().getIata());
            departureJson.put("icao", flight.getDeparture().getIcao());
            json.put("departure", departureJson);
        }

        if (flight.getArrival() != null) {
            JSONObject arrivalJson = new JSONObject();
            arrivalJson.put("name", flight.getArrival().getName());
            arrivalJson.put("iata", flight.getArrival().getIata());
            arrivalJson.put("icao", flight.getArrival().getIcao());
            json.put("arrival", arrivalJson);
        }

        if (flight.getLivePosition() != null) {
            JSONObject positionJson = new JSONObject();
            positionJson.put("latitude", flight.getLivePosition().getLatitude());
            positionJson.put("longitude", flight.getLivePosition().getLongitude());
            positionJson.put("altitude", flight.getLivePosition().getAltitude());
            positionJson.put("groundSpeed", flight.getLivePosition().getGroundSpeed());
            positionJson.put("track", flight.getLivePosition().getTrack());
            json.put("livePosition", positionJson);
        }

        return json;
    }

    private Flight deserializeFlight(JSONObject json) {
        try {
            String hex = json.getString("hex");
            String flightNumber = json.getString("flightNumber");
            String callsign = json.getString("callsign");

            Date eta = null;
            if (json.has("eta") && !json.isNull("eta")) {
                eta = new Date(json.getLong("eta"));
            }

            // Deserialize nested objects
            Airline airline = null;
            if (json.has("airline") && !json.isNull("airline")) {
                JSONObject airlineJson = json.getJSONObject("airline");
                airline = new Airline(
                        airlineJson.getString("iata"),
                        airlineJson.getString("icao"),
                        airlineJson.getString("name")
                );
            }

            Airport departure = null;
            if (json.has("departure") && !json.isNull("departure")) {
                JSONObject departureJson = json.getJSONObject("departure");
                departure = new Airport(
                        departureJson.getString("iata"),
                        departureJson.getString("icao"),
                        departureJson.getString("name")
                );
            }

            Airport arrival = null;
            if (json.has("arrival") && !json.isNull("arrival")) {
                JSONObject arrivalJson = json.getJSONObject("arrival");
                arrival = new Airport(
                        arrivalJson.getString("iata"),
                        arrivalJson.getString("icao"),
                        arrivalJson.getString("name")
                );
            }

            LivePosition livePosition = null;
            if (json.has("livePosition") && !json.isNull("livePosition")) {
                JSONObject positionJson = json.getJSONObject("livePosition");
                livePosition = new LivePosition(
                        positionJson.getDouble("latitude"),
                        positionJson.getDouble("longitude"),
                        positionJson.getInt("altitude"),
                        positionJson.getInt("groundSpeed"),
                        positionJson.getDouble("track")
                );
            }

            return new Flight(
                    hex, flightNumber, callsign, airline,
                    departure, arrival, eta, null, livePosition
            );
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Error deserializing flight from JSON", e);
            return null;
        }
    }

    // Additional utility methods
    public int getFavoriteCount() {
        return favorites.size();
    }

    public void clearAll() {
        favorites.clear();
        saveToFile();
        Logger.getInstance().info(CLASS_NAME, "Cleared all favorites");
    }

    public boolean isDataFileExists() {
        return Files.exists(dataPath);
    }
}