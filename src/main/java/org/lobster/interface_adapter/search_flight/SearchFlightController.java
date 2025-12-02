
package org.lobster.interface_adapter.search_flight;

import org.lobster.use_case.search_flight.SearchFlightInputBoundary;
import org.lobster.use_case.search_flight.SearchFlightInputData;

/**
 * Controller for the search flight use case.
 */
public class SearchFlightController {

    private final SearchFlightInputBoundary interactor;

    public SearchFlightController(SearchFlightInputBoundary interactor) {

        this.interactor = interactor;
    }

    /**
     * Handles a search request by passing the given flight number to the use case interactor.
     *
     * @param flightNumber "the flight number entered by the user for lookup"
     */
    public void onSearch(String flightNumber) {

        interactor.execute(new SearchFlightInputData(flightNumber));
    }
}
