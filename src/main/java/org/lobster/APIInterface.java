package org.lobster;

import org.json.*;

public interface APIInterface {
    // add method for fetch flight data, airport data
    JSONObject fetchFlightData(String flightNumber);
    JSONObject fetchAirportData(String airportCode);
}
