
package org.lobster.interface_adapter;

import org.lobster.entity.Airline;
import org.lobster.entity.Airport;
import org.lobster.entity.Flight;

public interface FlightDataAccessInterface {
    /**
     * Finds and returns a flight by its flight number.
     *
     * @param flightNumber the flight number identifier (e.g., "AC213")
     * @return a {@link Flight} matching the given flight number, or {@code null}
     *         if no such flight exists
     */
    Flight findByFlightNumber(String flightNumber);

    /**
     * Finds and returns a flight by its radio call sign.
     *
     * @param callsign the aircraft's callsign (e.g., "ACA213")
     * @return a {@link Flight} matching the given callsign, or {@code null}
     *         if none is found
     */
    Flight findByCallSign(String callsign);

    /**
     * Retrieves an airport entity using its ICAO code.
     *
     * @param icao the ICAO airport identifier (e.g., "CYYZ")
     * @return an {@link Airport} matching the ICAO code, or {@code null}
     *         if the airport is not found
     */
    Airport findAirportByIcao(String icao);

    /**
     * Retrieves an airline entity using its IATA code.
     *
     * @param iata the IATA airline identifier (e.g., "AC")
     * @return an {@link Airline} matching the IATA code, or {@code null}
     *         if no such airline exists
     */
    Airline findAirlineByIata(String iata);
}
