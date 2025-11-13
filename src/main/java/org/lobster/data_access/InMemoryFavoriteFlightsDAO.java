package org.lobster.data_access;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;

import java.util.*;

public class InMemoryFavoriteFlightsDAO implements FavoriteFlightsDataAccessInterface {
    private final Map<String, Flight> favorites = new HashMap<>();

    @Override
    public void save(Flight flight) {
        favorites.put(flight.getCallsign(), flight);
        System.out.println("Saved flight to favorites: " + flight.getCallsign());
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
        favorites.remove(flightNumber);
        System.out.println("Removed flight from favorites: " + flightNumber);
    }
}