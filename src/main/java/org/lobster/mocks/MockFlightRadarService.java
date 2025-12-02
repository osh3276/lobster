package org.lobster.mocks;

import org.json.JSONObject;
import org.lobster.data_access.FlightRadarService;

public class MockFlightRadarService extends FlightRadarService {

    private JSONObject airportToReturn;
    private boolean throwException = false;
    private String lastAirportCodeQueried;

    public void setAirportToReturn(JSONObject airportToReturn) {
        this.airportToReturn = airportToReturn;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public String getLastAirportCodeQueried() {
        return lastAirportCodeQueried;
    }

    @Override
    public JSONObject getAirport(String airportCode) {
        lastAirportCodeQueried = airportCode;

        if (throwException) {
            throw new RuntimeException("Mock error");
        }

        return airportToReturn;
    }
}