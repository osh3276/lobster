package org.lobster.interface_adapter;

import org.lobster.entity.Flight;
import java.util.List;

public interface AirportDataAccessInterface {
    List<Flight> getDepartures(String airportCode);
    List<Flight> getArrivals(String airportCode);
}