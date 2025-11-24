package org.lobster.mocks;

import org.lobster.entity.Flight;

import java.util.Date;

/**
 * Helper utilities for creating test data
 */
public class TestHelper {

    public static Flight createTestFlight(String flightNumber) {
        return new Flight(
                "hex" + flightNumber,           // hex
                flightNumber,                   // flightNumber
                "CAL" + flightNumber,           // callsign
                null,                           // airline
                null,                           // departure
                null,                           // arrival
                new Date(),                     // eta
                null,                           // status
                null                            // livePosition
        );
    }

    public static Flight createTestFlightWithDetails(String flightNumber, String hex, String callsign) {
        return new Flight(
                hex,                            // hex
                flightNumber,                   // flightNumber
                callsign,                       // callsign
                null,                           // airline
                null,                           // departure
                null,                           // arrival
                new Date(),                     // eta
                null,                           // status
                null                            // livePosition
        );
    }
}