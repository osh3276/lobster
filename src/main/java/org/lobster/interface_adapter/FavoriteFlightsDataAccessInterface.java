
package org.lobster.interface_adapter;

import java.util.List;

import org.lobster.entity.Flight;

public interface FavoriteFlightsDataAccessInterface {
    /**
     * Saves the given flight to the favorites list.
     *
     * @param flight the flight to save as a favorite
     */
    void save(Flight flight);

    /**
     * Checks whether a favorite flight exists with the specified flight number.
     *
     * @param flightNumber the flight number to search for
     */
    boolean existsByFlightNumber(String flightNumber);

    /**
     * Returns all flights currently stored as favorites.
     *
     * @return a list of all favorite flights
     */
    List<Flight> findAll();

    /**
     * Deletes the favorite flight associated with the given flight number.
     *
     * @param flightNumber the flight number of the flight to remove
     */
    void deleteByFlightNumber(String flightNumber);
}
