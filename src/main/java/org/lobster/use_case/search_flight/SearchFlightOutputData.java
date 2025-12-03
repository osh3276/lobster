package org.lobster.use_case.search_flight;

import org.lobster.entity.Flight;

/**
 * Output data for the Search Flight use case.
 */
public class SearchFlightOutputData {
    private final Flight flight;
    private final String message;

    /**
     * Constructs a new SearchFlightOutputData object.
     *
     * @param flight  the flight returned by the search, or {@code null}
     *                if no matching flight was found
     * @param message a message describing the result of the search
     */
    public SearchFlightOutputData(Flight flight, String message) {
        this.flight = flight;
        this.message = message;
    }
    /**
     * Returns the flight returned by the search.
     *
     * @return the found flight, or {@code null} if no result was found
     */
    public Flight getFlight() {
        return flight;
    }
    /**
     * Returns a message describing the result of the search.
     *
     * @return the result message
     */
    public String getMessage() {
        return message;
    }
}
