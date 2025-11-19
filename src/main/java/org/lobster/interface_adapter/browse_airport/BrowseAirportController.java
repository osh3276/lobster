package org.lobster.interface_adapter.browse_airport;

import org.lobster.use_case.browse_airport.BrowseAirportInputBoundary;
import org.lobster.use_case.browse_airport.BrowseAirportInputData;

public class BrowseAirportController {
    private final BrowseAirportInputBoundary browseAirportUseCase;

    public BrowseAirportController(BrowseAirportInputBoundary browseAirportUseCase) {
        this.browseAirportUseCase = browseAirportUseCase;
    }

    public void onBrowse(String airportCode, String type) {
        BrowseAirportInputData inputData = new BrowseAirportInputData(airportCode, type);
        browseAirportUseCase.execute(inputData);
    }
}
