package org.lobster.use_case.search_flight;

import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.entity.Flight;

/**
 * Interactor for the Search Flight use case.
 */
public class SearchFlightInteractor implements SearchFlightInputBoundary {
    private final FlightDataAccessInterface flightDataAccess;
    private final SearchFlightOutputBoundary presenter;

    /**
     * Constructs a new SearchFlightInteractor.
     *
     * @param flightDataAccess the data access interface used to retrieve flight
     *                         information
     * @param presenter        the output boundary responsible for presenting
     *                         search results
     */
    public SearchFlightInteractor(FlightDataAccessInterface flightDataAccess,
                                  SearchFlightOutputBoundary presenter) {
        this.flightDataAccess = flightDataAccess;
        this.presenter = presenter;
    }
    /**
     * Executes the Search Flight use case.
     *
     * @param inputData the input data containing the flight number
     */
    @Override
    public void execute(SearchFlightInputData inputData) {
        String f = inputData.getFlightNumber().trim().toUpperCase();

        if (f.isEmpty()) {
            presenter.present(new SearchFlightOutputData(null,
                    "Please enter a flight number"));
            return;
        }
        try {
            Flight flight = flightDataAccess.findByFlightNumber(f);
            if (flight == null) {
                presenter.present(new SearchFlightOutputData(null,
                        "No flight found for " + f));
            } else {
                presenter.present(new SearchFlightOutputData(flight,
                        "Flight found: " + f));
            }
        } catch (Exception e) {
            presenter.present(new SearchFlightOutputData(null,
                    "Error finding flight: " + e.getMessage()));
        }
    }
}
