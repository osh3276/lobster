package org.lobster.use_case.search_flight;

/**
 * Input data for the Search Flight use case.
 */
public class SearchFlightInputData {
    private final String flightNumber;

    /**
     * Constructs a new SearchFlightInputData object.
     *
     * @param flightNumber the flight number entered by the user
     */
    public SearchFlightInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    /**
     * Returns the flight number provided as input.
     *
     * @return the flight number as a string
     */
    public String getFlightNumber() {
        return flightNumber;
    }
}
