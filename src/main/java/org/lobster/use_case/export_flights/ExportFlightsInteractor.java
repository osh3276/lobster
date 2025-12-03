package org.lobster.use_case.export_flights;

import org.lobster.entity.Flight;
import org.lobster.entity.Airline;
import org.lobster.entity.Airport;
import org.lobster.entity.LivePosition;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * Interactor for the Export Flights use case.
 */
public class ExportFlightsInteractor implements ExportFlightsInputBoundary {
    private final ExportFlightsOutputBoundary outputBoundary;
    /**
     * Constructs a new {@code ExportFlightsInteractor}.
     *
     * @param outputBoundary the output boundary responsible for formatting
     *                       and presenting the result of the export operation
     */
    public ExportFlightsInteractor(ExportFlightsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    /**
     * Executes the Export Flights use case.
     *
     * <p>The method performs the following steps:
     * <ol>
     *   <li>Validates that the user selected flights to export.</li>
     *   <li>Validates that the export format is supported.</li>
     *   <li>Generates an appropriate filename and determines the export path.</li>
     *   <li>Writes the selected flights to the chosen file format.</li>
     *   <li>Returns success or failure to the presenter.</li>
     * </ol>
     *
     * @param inputData the input data containing the flights, format, and flags
     */
    @Override
    public void execute(ExportFlightsInputData inputData) {
        List<Flight> flights = inputData.getFlights();
        String exportFormat = inputData.getExportFormat();

        if (flights == null || flights.isEmpty()) {
            outputBoundary.prepareFailView("Please select at least one flight to export");
            return;
        }

        if (!"CSV".equalsIgnoreCase(exportFormat)) {
            outputBoundary.prepareFailView("Unsupported export format: " + exportFormat);
            return;
        }

        try {
            String fileName = generateFileName();
            Path downloadPath = getDownloadPath();
            Path filePath = downloadPath.resolve(fileName);

            exportToCSV(flights, filePath);

            String message = String.format("Successfully exported %d flight(s) to %s",
                    flights.size(), fileName);
            ExportFlightsOutputData outputData = new ExportFlightsOutputData(
                    true,
                    message,
                    filePath.toString(),
                    fileName
            );
            outputBoundary.prepareSuccessView(outputData);
        } catch (IOException e) {
            outputBoundary.prepareFailView("Failed to export flights: " + e.getMessage());
        }
    }
    /**
     * Writes the given flights to a CSV file at the specified path.
     *
     * @param flights  the flights to export
     * @param filePath the file path to write to
     * @throws IOException if writing fails
     */
    private void exportToCSV(List<Flight> flights, Path filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writeCSVHeader(writer);
            for (Flight flight : flights) {
                writeFlightToCSV(writer, flight);
            }
        }
    }
    /**
     * Writes the CSV header row.
     *
     * @param writer the CSV writer
     * @throws IOException if writing fails
     */
    private void writeCSVHeader(FileWriter writer) throws IOException {
        writer.append("Flight Number,Callsign,Airline IATA,Airline ICAO,Airline Name,")
              .append("Departure IATA,Departure ICAO,Departure Name,")
              .append("Arrival IATA,Arrival ICAO,Arrival Name,")
              .append("ETA,Status,")
              .append("Latitude,Longitude,Altitude (ft),Ground Speed (kt),Heading (deg),")
              .append("Hex\n");
    }
    /**
     * Writes a single flight record to the CSV output.
     *
     * @param writer the CSV writer
     * @param flight the flight to write
     * @throws IOException if writing fails
     */
    private void writeFlightToCSV(FileWriter writer, Flight flight) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        writer.append(escapeCSV(flight.getFlightNumber())).append(",")
              .append(escapeCSV(flight.getCallsign())).append(",");

        Airline airline = flight.getAirline();
        if (airline != null) {
            writer.append(escapeCSV(airline.getIata())).append(",")
                  .append(escapeCSV(airline.getIcao())).append(",")
                  .append(escapeCSV(airline.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Airport departure = flight.getDeparture();
        if (departure != null) {
            writer.append(escapeCSV(departure.getIata())).append(",")
                  .append(escapeCSV(departure.getIcao())).append(",")
                  .append(escapeCSV(departure.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Airport arrival = flight.getArrival();
        if (arrival != null) {
            writer.append(escapeCSV(arrival.getIata())).append(",")
                  .append(escapeCSV(arrival.getIcao())).append(",")
                  .append(escapeCSV(arrival.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Date eta = flight.getEta();
        if (eta != null) {
            writer.append(escapeCSV(dateFormat.format(eta))).append(",");
        } else {
            writer.append(",");
        }

        if (flight.getStatus() != null) {
            writer.append(escapeCSV(flight.getStatus().getDisplayName())).append(",");
        } else {
            writer.append(",");
        }

        LivePosition livePosition = flight.getLivePosition();
        if (livePosition != null && livePosition.isValid()) {
            writer.append(String.valueOf(livePosition.getLatitude())).append(",")
                  .append(String.valueOf(livePosition.getLongitude())).append(",")
                  .append(String.valueOf(livePosition.getAltitude())).append(",")
                  .append(String.valueOf(livePosition.getGroundSpeed())).append(",")
                  .append(String.valueOf(livePosition.getTrack())).append(",");
        } else {
            writer.append(",,,,,");
        }

        writer.append(escapeCSV(flight.getHex())).append("\n");
    }
    /**
     * Escapes values that contain commas, quotes, or line breaks
     * for safe CSV use.
     *
     * @param value the raw string value
     * @return the escaped value
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    /**
     * Generates a timestamped CSV filename.
     *
     * @return a unique filename for the export
     */
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "flights_export_" + dateFormat.format(new Date()) + ".csv";
    }
    /**
     * Determines a valid download directory (prefers ~/Downloads).
     *
     * @return a path where files can be safely saved
     * @throws IOException if filesystem access fails
     */
    private Path getDownloadPath() throws IOException {
        String userHome = System.getProperty("user.home");
        Path downloadPath = Paths.get(userHome, "Downloads");

        if (!Files.exists(downloadPath)) {
            downloadPath = Paths.get(userHome);
        }

        return downloadPath;
    }
}
