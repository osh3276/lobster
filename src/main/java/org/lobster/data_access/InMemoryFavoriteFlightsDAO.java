package org.lobster.data_access;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.util.Logger;

import java.util.*;

public class InMemoryFavoriteFlightsDAO implements FavoriteFlightsDataAccessInterface {
    
    private static final String CLASS_NAME = "InMemoryFavoriteFlightsDAO";
    private final Map<String, Flight> favorites = new HashMap<>();

    @Override
    public void save(Flight flight) {
        favorites.put(flight.getFlightNumber(), flight);
        Logger.getInstance().info(CLASS_NAME, "Saved flight to favorites: " + flight.getCallsign());
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
            Logger.getInstance().info(CLASS_NAME, "Removed flight from favorites: " + flightNumber);
        } else {
            Logger.getInstance().warn(CLASS_NAME, "Attempted to remove non-existent flight: " + flightNumber);
        }
    }
}