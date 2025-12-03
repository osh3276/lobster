package org.lobster.use_case.browse_airport;

import org.json.JSONObject;
import org.lobster.data_access.FlightRadarService;
import org.lobster.entity.Airport;

/**
 * Interactor for the Browse Airport use case.
 */
public class BrowseAirportInteractor implements BrowseAirportInputBoundary {

    private final FlightRadarService flightService;
    private final BrowseAirportOutputBoundary presenter;
    /**
     * Constructs the interactor with its required dependencies.
     *
     * @param flightService the service used to retrieve airport data
     * @param presenter the output boundary responsible for formatting the result
     */
    public BrowseAirportInteractor(FlightRadarService flightService, BrowseAirportOutputBoundary presenter) {
        this.flightService = flightService;
        this.presenter = presenter;
    }
    /**
     * Executes the Browse Airport use case.
     *
     * <p>The steps are:
     * <ol>
     *     <li>Validate the airport code.</li>
     *     <li>Call the {@link FlightRadarService} to retrieve airport data.</li>
     *     <li>If no data is found, return a failure message.</li>
     *     <li>If valid, construct an {@link Airport} entity.</li>
     *     <li>Send the output to the presenter.</li>
     * </ol>
     *
     * @param inputData the input data containing the airport code
     */
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
