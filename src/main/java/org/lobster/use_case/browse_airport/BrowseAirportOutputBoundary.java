package org.lobster.use_case.browse_airport;
/**
 * Output boundary for the Browse Airport use case.
 */
public interface BrowseAirportOutputBoundary {
    /**
     * Presents the result of the Browse Airport use case.
     *
     * @param data the output data containing the airport (if found)
     *             and a descriptive message
     */
    void present(BrowseAirportOutputData data);
}
