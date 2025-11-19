package org.lobster.use_case.export_flights;

public interface ExportFlightsOutputBoundary {
    void prepareSuccessView(ExportFlightsOutputData outputData);
    void prepareFailView(String errorMessage);
}

