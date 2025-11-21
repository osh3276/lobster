package org.lobster.interface_adapter.browse_airport;

import org.lobster.entity.Flight;
import java.util.List;

public class BrowseAirportViewModel {
    private List<Flight> flights;
    private String message;

    public List<Flight> getFlights() {
        return flights;
    }

    public String getMessage(){
        return message;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
