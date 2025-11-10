package org.lobster.interface_adapter;

import org.lobster.entity.Flight;

public interface FlightDataAccessInterface {
    Flight findByFlightNumber(String flightNumber);
}