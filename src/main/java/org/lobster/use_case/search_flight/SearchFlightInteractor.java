package org.lobster.use_case.search_flight;

import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.entity.Flight;

public class SearchFlightInteractor implements SearchFlightInputBoundary{
    private final FlightDataAccessInterface flightDataAccess;
    private final SearchFlightOutputBoundary presenter;

    public SearchFlightInteractor(FlightDataAccessInterface flightDataAccess, SearchFlightOutputBoundary presenter) {
        this.flightDataAccess = flightDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchFlightInputData inputData) {
        String f = inputData.getFlightNumber().trim().toUpperCase();

        if (f.isEmpty()){
            presenter.present(new SearchFlightOutputData(null, "Please enter a flight number"));
            return;
        }

        presenter.present(new SearchFlightOutputData(null, "Search not implemented yet for " +f));
    }
}
