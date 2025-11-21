package org.lobster.interface_adapter.export_flights;

import org.lobster.entity.Flight;
import org.lobster.use_case.export_flights.ExportFlightsInputBoundary;
import org.lobster.use_case.export_flights.ExportFlightsInputData;

import java.util.List;

public class ExportFlightsController {
    private final ExportFlightsInputBoundary exportFlightsUseCase;

    public ExportFlightsController(ExportFlightsInputBoundary exportFlightsUseCase) {
        this.exportFlightsUseCase = exportFlightsUseCase;
    }

    public void execute(List<Flight> flights, String exportFormat, boolean exportAllFavorites) {
        ExportFlightsInputData inputData = new ExportFlightsInputData(flights, exportFormat, exportAllFavorites);
        exportFlightsUseCase.execute(inputData);
    }
}

