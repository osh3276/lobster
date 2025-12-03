package org.lobster.use_case.browse_airport;
/**
 * Input boundary for the Browse Airport use case.
 */
public interface BrowseAirportInputBoundary {
    /**
     * Executes the Browse Airport use case using the provided input data.
     *
     * @param data the request model containing the airport code
     */
    void execute(BrowseAirportInputData data);
}
