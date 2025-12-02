package org.lobster.use_case.browse_airport;

import org.lobster.entity.Airport;
/**
 * Output data for the Browse Airport use case.
 */
public class BrowseAirportOutputData {
    /**
     * The airport retrieved by the use case.
     */
    private final Airport airport;
    /**
     * A message describing the result of the operation.
     * This may describe success or an error condition.
     */
    private final String message;
    /**
     * Constructs a {@code BrowseAirportOutputData} object.
     *
     * @param airport the airport found during the use case execution;
     *                may be {@code null} if the lookup failed
     * @param message a descriptive message for the presenter and UI
     */
    public BrowseAirportOutputData(Airport airport, String message) {
        this.airport = airport;
        this.message = message;
    }
    /**
     * Returns the airport returned by the use case.
     *
     * @return the airport, or {@code null} if no airport was found
     */
    public Airport getAirport() {
        return airport;
    }
    /**
     * Returns the message describing the result of the airport lookup.
     *
     * @return a descriptive message
     */
    public String getMessage() {
        return message;
    }
}
