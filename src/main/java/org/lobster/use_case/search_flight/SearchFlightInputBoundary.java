package org.lobster.use_case.search_flight;

/**
 * Input boundary for the Search Flight use case.
 */
public interface SearchFlightInputBoundary {
    /**
     * Executes the Search Flight use case using the provided input data.
     *
     * @param data the input data containing the flight number to be searched
     */
    void execute(SearchFlightInputData data);
}
