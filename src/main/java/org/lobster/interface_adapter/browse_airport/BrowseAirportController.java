
package org.lobster.interface_adapter.browse_airport;

import org.lobster.use_case.browse_airport.BrowseAirportInputBoundary;
import org.lobster.use_case.browse_airport.BrowseAirportInputData;

/**
 * Controller for the Browse by Aiport use case.
 */
public class BrowseAirportController {
    private final BrowseAirportInputBoundary browseAirportUseCase;

    public BrowseAirportController(BrowseAirportInputBoundary browseAirportUseCase) {
        this.browseAirportUseCase = browseAirportUseCase;
    }

    /**
     *  Execution command for the browseaiport controller.
     * @param airportCode "Airport code"
     * @param type "Arrival or Departure"
     */
    public void onBrowse(String airportCode, String type) {
        BrowseAirportInputData inputData = new BrowseAirportInputData(airportCode, type);
        browseAirportUseCase.execute(inputData);
    }
}
