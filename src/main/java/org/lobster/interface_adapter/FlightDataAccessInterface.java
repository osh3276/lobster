package org.lobster.interface_adapter;

import org.lobster.entity.Airline;
import org.lobster.entity.Airport;
import org.lobster.entity.Flight;

public interface FlightDataAccessInterface {
    Flight findByFlightNumber(String flightNumber);

    Flight findByCallSign(String callsign);

    Airport findAirportByIcao(String icao);

    Airline findAirlineByIata(String iata);
}