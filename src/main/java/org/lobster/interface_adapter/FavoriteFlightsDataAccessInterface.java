package org.lobster.interface_adapter;

import org.lobster.entity.Flight;
import java.util.List;

public interface FavoriteFlightsDataAccessInterface {
    void save(Flight flight);
    boolean existsByFlightNumber(String flightNumber);
    List<Flight> findAll();
    void deleteByFlightNumber(String flightNumber);
}