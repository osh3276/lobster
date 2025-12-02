package org.lobster.use_case.status_change;

import org.lobster.entity.Flight;

/**
 * Output data for the Status Change use case.
 */
public class StatusChangeOutputData {
    private final Flight updatedFlight;

    /**
     * Constructs an output data object containing the updated flight.
     *
     * @param updatedFlight the flight whose status has just changed
     */
    public StatusChangeOutputData(Flight updatedFlight) {
        this.updatedFlight = updatedFlight;
    }

    /**
     * Returns the flight that has been updated.
     *
     * @return the updated flight entity
     */
    public Flight getUpdatedFlight() {
        return updatedFlight;
    }
}
