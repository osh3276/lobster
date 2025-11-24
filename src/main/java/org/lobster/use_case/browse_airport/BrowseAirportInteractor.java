package org.lobster.use_case.browse_airport;

import org.lobster.data_access.FlightRadarService;
import org.lobster.entity.Flight;
import org.lobster.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class BrowseAirportInteractor implements BrowseAirportInputBoundary {

    private final FlightRadarService flightService;
    private final BrowseAirportOutputBoundary presenter;
    private static final String CLASS_NAME = "BrowseAirportInteractor";

    public BrowseAirportInteractor(FlightRadarService flightService, BrowseAirportOutputBoundary presenter) {
        this.flightService = flightService;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowseAirportInputData inputData) {
        String airport = inputData.getAirportCode();
        String type = inputData.getType();

        if (airport.isEmpty()) {
            presenter.present(new BrowseAirportOutputData(new ArrayList<>(),
                    "Please enter an airport code."));
            return;
        }

        if (!type.equals("arrivals") && !type.equals("departures")) {
            presenter.present(new BrowseAirportOutputData(new ArrayList<>(),
                    "Type must be 'arrivals' or 'departures'."));
            return;
        }

        Logger.getInstance().info(CLASS_NAME, "Fetching " + type + " for aiport " + airport);

        List<Flight> flights;
        try {
            flights = flightService.getFlightsByAirport(airport, type);
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME,
                    "API request failed: " + e.getMessage(), e);

            presenter.present(new BrowseAirportOutputData(
                    new ArrayList<>(),
                    "Failed to load flights for " + airport));
            return;
        }

        if (flights == null || flights.isEmpty()) {
            presenter.present(new BrowseAirportOutputData(new ArrayList<>(),
                    "No " + type + " currently available for " + airport + "."));
            return;
        }

        presenter.present(new BrowseAirportOutputData(
                flights,
                "Showing " + type + " for " + airport + "."));
    }
}
