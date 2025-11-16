package org.lobster.interface_adapter.search_flight;

import org.lobster.entity.Flight;

public class SearchFlightViewModel {
    private Flight flight;
    private String message;

    public Flight getFlight() {
        return flight;
    }

    public String getMessage() {
        return message;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
