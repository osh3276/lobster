package org.lobster.interface_adapter.export_flights;

import org.lobster.use_case.export_flights.ExportFlightsOutputBoundary;
import org.lobster.use_case.export_flights.ExportFlightsOutputData;

import javax.swing.JOptionPane;

public class ExportFlightsPresenter implements ExportFlightsOutputBoundary {

    public ExportFlightsPresenter() {
    }

    @Override
    public void prepareSuccessView(ExportFlightsOutputData outputData) {
        String message = outputData.getMessage() + "\n" +
                "File: " + outputData.getFileName() + "\n" +
                "Location: " + outputData.getFilePath();
        JOptionPane.showMessageDialog(
                null,
                message,
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(
                null,
                errorMessage,
                "Export Failed",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

