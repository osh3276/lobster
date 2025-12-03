package org.lobster.use_case.search_flight;

/**
 * Output boundary for the Search Flight use case.
 */
public interface SearchFlightOutputBoundary {
    /**
     * Presents the results of the Search Flight use case.
     *
     * @param data the output data containing either the found flight
     *             or a message describing why the search failed
     */
    void present(SearchFlightOutputData data);
}
