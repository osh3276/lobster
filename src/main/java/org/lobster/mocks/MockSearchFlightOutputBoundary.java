package org.lobster.mocks;

import org.lobster.use_case.search_flight.SearchFlightOutputBoundary;
import org.lobster.use_case.search_flight.SearchFlightOutputData;

public class MockSearchFlightOutputBoundary implements SearchFlightOutputBoundary {

    public SearchFlightOutputData lastOutput = null;
    public int callCount = 0;

    public void reset() {
        lastOutput = null;
        callCount = 0;
    }

    @Override
    public void present(SearchFlightOutputData data) {
        this.lastOutput = data;
        callCount++;
    }
}

