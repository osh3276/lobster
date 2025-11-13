// File: src/main/java/org/lobster/data_access/FlightRadarDataAccess.java
package org.lobster.data_access;

import org.json.JSONObject;
import org.lobster.entity.*;
import org.lobster.interface_adapter.FlightDataAccessInterface;

import java.util.Date;

public class FlightRadarDataAccess implements FlightDataAccessInterface {

    private final FlightRadarService service;

    public FlightRadarDataAccess() {
        this.service = new FlightRadarService();
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        JSONObject flightInfo = service.findByFlightNumber(flightNumber);
        return mapApiResponseToFlight(flightInfo);
    }

    @Override
    public Flight findByCallSign(String callsign) {
        JSONObject flightInfo = service.findByCallsign(callsign);
        return mapApiResponseToFlight(flightInfo);
    }

    @Override
    public Airport findAirportByIcao(String icao) {
        return mapApiResponseToAirport(service.getAirport(icao));
    }

    @Override
    public Airline findAirlineByIata(String iata) {
        return mapApiResponseToAirline(service.getAirline(iata));
    }

    // probably not possible, might just fake the data lolz
    public Flight[] returnAllFlights() {
        return null;
    }

    private Flight mapApiResponseToFlight(JSONObject flightInfo) {
        String hex = flightInfo.getString("hex");
        String flightNum = flightInfo.getString("flight");
        String callsign = flightInfo.getString("callsign");
        double latitude = flightInfo.getDouble("lat");
        double longitude = flightInfo.getDouble("lon");
        double track = flightInfo.getDouble("track");
        int altitude = flightInfo.getInt("altitude");
        int groundSpeed = flightInfo.getInt("gspeed");
        String origIata = flightInfo.getString("orig_iata");
        String destIata = flightInfo.getString("dest_iata");
        String operatingAs = flightInfo.getString("operating_as");
        Date eta = new Date(Long.parseLong(flightInfo.getString("eta")));

        Airline airline = mapApiResponseToAirline(service.getAirline(operatingAs));
        Airport departure = mapApiResponseToAirport(service.getAirport(origIata));
        Airport arrival = mapApiResponseToAirport(service.getAirport(destIata));

        LivePosition livePosition = new LivePosition(
                latitude,
                longitude,
                altitude,
                groundSpeed,
                track
        );

        return new Flight(
                hex,                // hex
                flightNum,          // flightNumber
                callsign,           // callsign
                airline,            // airline
                departure,          // departure
                arrival,            // arrival
                eta,                // eta
                null,               // status - not available in response
                livePosition        // livePosition
        );
    }

    private Airport mapApiResponseToAirport(JSONObject responseJson) {
        if (responseJson == null) return null;
        
        String name = responseJson.getString("name");
        String iata = responseJson.getString("iata");
        String icao = responseJson.getString("icao");

        return new Airport(iata, icao, name);
    }

    private Airline mapApiResponseToAirline(JSONObject responseJson) {
        if (responseJson == null) return null;
        
        String name = responseJson.getString("name");
        String iata = responseJson.getString("iata");
        String icao = responseJson.getString("icao");

        return new Airline(iata, icao, name);
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
