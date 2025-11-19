package org.lobster.use_case.browse_airport;

import org.lobster.data_access.FlightRadarService;
import org.lobster.entity.Flight;

import java.util.List;

public class BrowseAirportInteractor implements BrowseAirportInputBoundary {

    private final FlightRadarService flightService;
    private final BrowseAirportOutputBoundary presenter;

    public BrowseAirportInteractor(FlightRadarService flightService, BrowseAirportOutputBoundary presenter) {
        this.flightService = flightService;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowseAirportInputData inputData) {
        String airport = inputData.getAirportCode();
        String type = inputData.getType();

        if (airport == null || airport.isEmpty()) {
            presenter.present(new BrowseAirportOutputData(null, "Please enter an airport code."));
            return;

            if (!type.equals("arrivals") && !type.equals("departures")) {
                presenter.present(new BrowseAirportOutputData(null, "Type must be 'arrivals' or 'departures'."));
                return;
            }

            List<Flight> flights = flightService.getFlightsByAirport(airport, type);

            if (flights == null || flights.isEmpty()) {
                presenter.present(new BrowseAirportOutputData(null, "No " + type + " currently available for " + airport + "."));
                return;
            }

            presenter.present(new BrowseAirportOutputData(flights, "Showing " + type + " for " + airport + "."));
        }
    }
}
