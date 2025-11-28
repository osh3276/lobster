package org.lobster.mocks;

import org.lobster.use_case.search_flight.SearchFlightInputBoundary;
import org.lobster.use_case.search_flight.SearchFlightInputData;

public class MockSearchFlightInputBoundary implements SearchFlightInputBoundary {

    public SearchFlightInputData lastInput = null;
    public int callCount = 0;

    @Override
    public void execute(SearchFlightInputData data) {
        lastInput = data;
        callCount++;
    }

    public void reset() {
        lastInput = null;
        callCount = 0;
    }
}

