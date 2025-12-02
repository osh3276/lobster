
package org.lobster.interface_adapter.export_flights;

import java.util.List;

import org.lobster.entity.Flight;
import org.lobster.use_case.export_flights.ExportFlightsInputBoundary;
import org.lobster.use_case.export_flights.ExportFlightsInputData;

/**
 * Controller for the Export Flights Use Case.
 */
public class ExportFlightsController {
    private final ExportFlightsInputBoundary exportFlightsUseCase;

    /**
     * Constructs the controller with the provided use case interactor.
     * @param exportFlightsUseCase "the interactor that executes the Export Flights use case"
     */
    public ExportFlightsController(ExportFlightsInputBoundary exportFlightsUseCase) {
        this.exportFlightsUseCase = exportFlightsUseCase;
    }

    /**
     * Executes the Export Flights use case.
     * @param flights "the list of flights to export"
     * @param exportFormat "the desired export format (e.g., "csv", "json")"
     * @param exportAllFavorites "whether the user selected to export all favorite flights"
     */
    public void execute(List<Flight> flights, String exportFormat, boolean exportAllFavorites) {
        ExportFlightsInputData inputData = new ExportFlightsInputData(flights, exportFormat, exportAllFavorites);
        exportFlightsUseCase.execute(inputData);
    }
}
