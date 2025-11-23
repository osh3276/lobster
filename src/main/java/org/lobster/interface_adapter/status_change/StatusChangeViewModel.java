package org.lobster.interface_adapter.status_change;

import org.lobster.entity.Flight;

public class StatusChangeViewModel {

    private Flight lastUpdatedFlight;

    public void setLastUpdatedFlight(Flight f) {
        this.lastUpdatedFlight = f;
    }

    public Flight getLastUpdatedFlight() {
        return lastUpdatedFlight;
    }

    public void notifyObservers() {
        System.out.println("Status updated: " + lastUpdatedFlight.getStatus());
    }
}
