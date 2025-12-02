package org.lobster.use_case.export_flights;
/**
 * Input boundary for the Export Flights use case.
 */
public interface ExportFlightsInputBoundary {
    /**
     * Executes the Export Flights use case.
     *
     * @param inputData the data required to perform the export operation,
     *                  including the selected flights, export format, and
     *                  export options
     */
    void execute(ExportFlightsInputData inputData);
}

