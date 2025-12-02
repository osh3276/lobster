package org.lobster.interface_adapter.status_change;

import org.lobster.entity.Flight;

public class StatusChangeViewModel {

    private Flight lastUpdatedFlight;

    public void setLastUpdatedFlight(Flight flight) {
        this.lastUpdatedFlight = flight;
    }

    public Flight getLastUpdatedFlight() {
        return lastUpdatedFlight;
    }

    /**
     * Notifies observers that a flight status has been updated.
     */
    public void notifyObservers() {
        System.out.println("Status updated: " + lastUpdatedFlight.getStatus());
    }
}
