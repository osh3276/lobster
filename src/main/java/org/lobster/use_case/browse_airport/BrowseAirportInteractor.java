package org.lobster.use_case.browse_airport;

import org.json.JSONObject;
import org.lobster.data_access.FlightRadarService;
import org.lobster.entity.Airport;

public class BrowseAirportInteractor implements BrowseAirportInputBoundary {

    private final FlightRadarService flightService;
    private final BrowseAirportOutputBoundary presenter;

    public BrowseAirportInteractor(FlightRadarService flightService, BrowseAirportOutputBoundary presenter) {
        this.flightService = flightService;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowseAirportInputData inputData) {

        String code = inputData.getAirportCode();

        if (code == null || code.isEmpty()) {
            presenter.present(new BrowseAirportOutputData(
                    null,
                    "Please enter an airport code."
            ));
            return;
        }

        var json = flightService.getAirport(code);

        if (json == null) {
            presenter.present(new BrowseAirportOutputData(
                    null,
                    "No information found for: " + code
            ));
            return;
        }

        Airport airport = new Airport(
                json.optString("iata", ""),
                json.optString("icao", ""),
                json.optString("name", "Unknown airport")
        );

        presenter.present(new BrowseAirportOutputData(airport, ""));
    }
}
