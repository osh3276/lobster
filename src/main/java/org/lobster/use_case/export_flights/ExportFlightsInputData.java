package org.lobster.use_case.export_flights;

import org.lobster.entity.Flight;
import java.util.List;

public class ExportFlightsInputData {
    private final List<Flight> flights;
    private final String exportFormat;
    private final boolean exportAllFavorites;

    public ExportFlightsInputData(List<Flight> flights, String exportFormat, boolean exportAllFavorites) {
        this.flights = flights;
        this.exportFormat = exportFormat;
        this.exportAllFavorites = exportAllFavorites;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getExportFormat() {
        return exportFormat;
    }

    public boolean isExportAllFavorites() {
        return exportAllFavorites;
    }
}

