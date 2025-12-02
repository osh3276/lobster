package org.lobster.use_case.export_flights;
/**
 * Output boundary for the Export Flights use case.
 */
public interface ExportFlightsOutputBoundary {
    /**
     * Prepares the success view when flight data is successfully exported.
     *
     * @param outputData the output data containing
     *                   file details and a result message
     */
    void prepareSuccessView(ExportFlightsOutputData outputData);
    /**
     * Prepares the failure view when the export operation fails.
     *
     * @param errorMessage a descriptive message explaining the failure
     */
    void prepareFailView(String errorMessage);
}
