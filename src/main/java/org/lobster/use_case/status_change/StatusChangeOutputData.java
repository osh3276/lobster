package org.lobster.use_case.status_change;

import org.lobster.entity.Flight;

public class StatusChangeOutputData {
    private final Flight updatedFlight;

    public StatusChangeOutputData(Flight updatedFlight) {
        this.updatedFlight = updatedFlight;
    }

    public Flight getUpdatedFlight() {
        return updatedFlight;
    }
}
