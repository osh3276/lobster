package org.lobster.interface_adapter.search_flight;

import org.lobster.use_case.search_flight.SearchFlightInputBoundary;
import org.lobster.use_case.search_flight.SearchFlightInputData;

public class SearchFlightController {

    private final SearchFlightInputBoundary interactor;

    public SearchFlightController(SearchFlightInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onSearch(String flightNumber){
        interactor.execute(new SearchFlightInputData(flightNumber));
    }
}
