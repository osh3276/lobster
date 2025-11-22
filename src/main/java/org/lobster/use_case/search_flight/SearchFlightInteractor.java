package org.lobster.use_case.search_flight;

import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.entity.Flight;
import org.lobster.util.Logger;
import org.lobster.exception.FlightDataException;

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
try {
    Flight flight = flightDataAccess.findByCallSign(f);

    if (flight == null) {
        presenter.present(new SearchFlightOutputData(null, "No flight found for " + f));
    }
    else {
        presenter.present(new SearchFlightOutputData(flight, "Flight found: " +f ));
    }
} catch (Exception e) {
    presenter.present(new SearchFlightOutputData(null, "Error finding flight: " + e.getMessage()));
}
    }
}
