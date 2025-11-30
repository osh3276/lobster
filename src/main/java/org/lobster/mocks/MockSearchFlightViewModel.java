package org.lobster.mocks;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.search_flight.SearchFlightViewModel;

public class MockSearchFlightViewModel extends SearchFlightViewModel {

    public Flight lastFlight = null;
    public String lastMessage = null;
    public int flightSetCount = 0;
    public int messageSetCount = 0;

    @Override
    public void setFlight(Flight flight) {
        this.lastFlight = flight;
        flightSetCount++;
    }

    @Override
    public void setMessage(String message) {
        this.lastMessage = message;
        messageSetCount++;
    }

    public void reset() {
        lastFlight = null;
        lastMessage = null;
        flightSetCount = 0;
        messageSetCount = 0;
    }
}

