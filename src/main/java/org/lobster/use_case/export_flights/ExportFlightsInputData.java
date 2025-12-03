package org.lobster.use_case.export_flights;

import org.lobster.entity.Flight;
import java.util.List;
/**
 * Input data for the Export Flights use case.
 */
public class ExportFlightsInputData {
    private final List<Flight> flights;
    private final String exportFormat;
    private final boolean exportAllFavorites;
    /**
     * Constructs a new {@code ExportFlightsInputData} object.
     *
     * @param flights            the list of flights selected for export
     * @param exportFormat       the format to export in (e.g., "CSV")
     * @param exportAllFavorites whether all favorites should be exported
     */
    public ExportFlightsInputData(List<Flight> flights, String exportFormat, boolean exportAllFavorites) {
        this.flights = flights;
        this.exportFormat = exportFormat;
        this.exportAllFavorites = exportAllFavorites;
    }
    /**
     * Returns the list of flights selected for export.
     *
     * @return a list of {@link Flight} objects
     */
    public List<Flight> getFlights() {
        return flights;
    }
    /**
     * Returns the export format requested by the user.
     *
     * @return the export format as a string
     */
    public String getExportFormat() {
        return exportFormat;
    }
    /**
     * Returns whether all favorite flights should be exported.
     *
     * @return {@code true} if all favorites should be exported; {@code false} otherwise
     */
    public boolean isExportAllFavorites() {
        return exportAllFavorites;
    }
}
