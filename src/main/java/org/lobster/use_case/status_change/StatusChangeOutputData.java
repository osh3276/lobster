package org.lobster.use_case.status_change;

import org.lobster.entity.Flight;

public class StatusChangeOutputData {
    private final Flight updatdFlight;

    public StatusChangeOutputData(Flight updatdFlight) {
        this.updatdFlight = updatdFlight;
    }

    public Flight getUpdatdFlight() {
        return updatdFlight;
    }
}
