package org.lobster.use_case.export_flights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.lobster.entity.*;
import java.util.*;
import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExportFlightsInteractor Tests")
class ExportFlightsInteractorTest {

    static class TestPresenter implements ExportFlightsOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        ExportFlightsOutputData successData = null;
        String failMessage = null;

        @Override
        public void prepareSuccessView(ExportFlightsOutputData outputData) {
            this.successCalled = true;
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.failMessage = errorMessage;
        }

        void reset() {
            successCalled = false;
            failCalled = false;
            successData = null;
            failMessage = null;
        }
    }

    private TestPresenter presenter;
    private ExportFlightsInteractor interactor;

    @BeforeEach
    void setUp() {
        presenter = new TestPresenter();
        interactor = new ExportFlightsInteractor(presenter);
    }

    private Flight createFlight() {
        Airline airline = new Airline("AC", "ACA", "Air Canada");
        Airport dep = new Airport("YYZ", "CYYZ", "Toronto Pearson");
        Airport arr = new Airport("YVR", "CYVR", "Vancouver");
        LivePosition pos = new LivePosition(43.677, -79.624, 30000, 450, 180);

        return new Flight("ABC123", "AC001", "ACA001", airline, dep, arr,
                new Date(), FlightStatus.IN_AIR, pos);
    }

    private Flight createFlightWithNulls() {
        return new Flight("DEF456", "AC002", "ACA002", null, null, null,
                null, null, null);
    }

    @Test
    @DisplayName("Execute with null flights list should prepare fail view")
    void testNullFlightListFails() {
        ExportFlightsInputData input = new ExportFlightsInputData(null, "CSV", false);
        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertFalse(presenter.successCalled);
        assertEquals("Please select at least one flight to export", presenter.failMessage);
    }

    @Test
    @DisplayName("Execute with empty flights list should prepare fail view")
    void testEmptyFlightListFails() {
        ExportFlightsInputData input = new ExportFlightsInputData(
                Collections.emptyList(), "CSV", false);
        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("Please select at least one flight to export", presenter.failMessage);
    }

    @Test
    @DisplayName("Execute with unsupported format should prepare fail view")
    void testUnsupportedFormatFails() {
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(createFlight()), "JSON", false);
        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("Unsupported export format: JSON", presenter.failMessage);
    }

    @Test
    @DisplayName("Execute with CSV format (case insensitive) should succeed")
    void testCsvFormatCaseInsensitive() throws IOException {
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(createFlight()), "csv", false);
        interactor.execute(input);

        assertTrue(presenter.successCalled);
        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("Execute with single flight should export successfully")
    void testSuccessfulExportSingleFlight() throws IOException {
        Flight flight = createFlight();
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);

        interactor.execute(input);

        assertTrue(presenter.successCalled);
        assertTrue(presenter.successData.isSuccess());
        assertTrue(presenter.successData.getMessage().contains("Successfully exported"));
        assertTrue(presenter.successData.getMessage().contains("1 flight(s)"));
        assertTrue(presenter.successData.getFilePath().endsWith(".csv"));

        Path filePath = Paths.get(presenter.successData.getFilePath());
        assertTrue(Files.exists(filePath));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("Execute with multiple flights should export successfully")
    void testSuccessfulExportMultipleFlights() throws IOException {
        Flight flight1 = createFlight();
        Flight flight2 = createFlightWithNulls();
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight1, flight2), "CSV", false);

        interactor.execute(input);

        assertTrue(presenter.successCalled);
        assertTrue(presenter.successData.getMessage().contains("2 flight(s)"));

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("AC001"));
        assertTrue(content.contains("AC002"));

        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("CSV file should contain correct headers")
    void testCsvFileContainsHeaders() throws IOException {
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(createFlight()), "CSV", false);

        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);

        assertTrue(content.contains("Flight Number"));
        assertTrue(content.contains("Latitude"));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("ExportFlightsOutputData constructor and getters")
    void testExportFlightsOutputData() {
        ExportFlightsOutputData outputData = new ExportFlightsOutputData(
                true, "Test message", "/test/path/file.csv", "file.csv");

        assertTrue(outputData.isSuccess());
        assertEquals("Test message", outputData.getMessage());
        assertEquals("/test/path/file.csv", outputData.getFilePath());
        assertEquals("file.csv", outputData.getFileName());
    }

    @Test
    @DisplayName("ExportFlightsInputData getters")
    void testExportFlightsInputDataGetters() {
        Flight flight = createFlight();
        List<Flight> flights = List.of(flight);
        ExportFlightsInputData input = new ExportFlightsInputData(flights, "CSV", true);

        assertEquals(flights, input.getFlights());
        assertEquals("CSV", input.getExportFormat());
        assertTrue(input.isExportAllFavorites());
    }

    @Test
    @DisplayName("ExportFlightsInputBoundary interface implementation")
    void testExportFlightsInputBoundaryImplementation() throws IOException {
        assertTrue(interactor instanceof ExportFlightsInputBoundary);

        ExportFlightsInputBoundary boundary = interactor;
        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(createFlight()), "CSV", false);

        boundary.execute(input);
        assertTrue(presenter.successCalled);
        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("CSV should handle flight with null airline")
    void testCsvWithNullAirline() throws IOException {
        Flight flight = new Flight("GHI789", "AC003", "ACA003", null,
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.SCHEDULED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("AC003"));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("CSV should handle flight with null departure")
    void testCsvWithNullDeparture() throws IOException {
        Flight flight = new Flight("JKL012", "AC004", "ACA004",
                new Airline("AC", "ACA", "Air Canada"), null,
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.IN_AIR, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("CSV should handle flight with null arrival")
    void testCsvWithNullArrival() throws IOException {
        Flight flight = new Flight("MNO345", "AC005", "ACA005",
                new Airline("AC", "ACA", "Air Canada"),
                new Airport("YYZ", "CYYZ", "Toronto"), null,
                new Date(), FlightStatus.DELAYED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("CSV should handle flight with null ETA")
    void testCsvWithNullEta() throws IOException {
        Flight flight = new Flight("PQR678", "AC006", "ACA006",
                new Airline("AC", "ACA", "Air Canada"),
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                null, FlightStatus.CANCELLED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("CSV should handle flight with null status")
    void testCsvWithNullStatus() throws IOException {
        Flight flight = new Flight("STU901", "AC007", "ACA007",
                new Airline("AC", "ACA", "Air Canada"),
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), null, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("CSV should handle flight with invalid LivePosition")
    void testCsvWithInvalidLivePosition() throws IOException {
        LivePosition invalidPos = new LivePosition(200, 300, 0, 0, 0);
        Flight flight = new Flight("VWX234", "AC008", "ACA008",
                new Airline("AC", "ACA", "Air Canada"),
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.IN_AIR, invalidPos);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }

    @Test
    @DisplayName("CSV should handle flight with valid LivePosition")
    void testCsvWithValidLivePosition() throws IOException {
        LivePosition validPos = new LivePosition(43.677, -79.624, 35000, 500, 90);
        Flight flight = new Flight("BCD890", "AC010", "ACA010",
                new Airline("AC", "ACA", "Air Canada"),
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.IN_AIR, validPos);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("43.677"));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("escapeCSV should handle values with commas")
    void testCsvEscapeCommas() throws IOException {
        Airline airline = new Airline("AC, Inc", "ACA", "Air Canada");
        Flight flight = new Flight("EFG123", "AC011", "ACA011", airline,
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.SCHEDULED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("\"AC, Inc\""));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("escapeCSV should handle values with quotes")
    void testCsvEscapeQuotes() throws IOException {
        Airline airline = new Airline("AC", "ACA", "Air \"Canada\"");
        Flight flight = new Flight("HIJ456", "AC012", "ACA012", airline,
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.SCHEDULED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("\"Air \"\"Canada\"\"\""));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("escapeCSV should handle values with newlines")
    void testCsvEscapeNewlines() throws IOException {
        Airline airline = new Airline("AC", "ACA", "Air\nCanada");
        Flight flight = new Flight("KLM789", "AC013", "ACA013", airline,
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.SCHEDULED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        assertTrue(content.contains("\"Air\nCanada\""));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("escapeCSV should handle normal values without special characters")
    void testCsvEscapeNormalValue() throws IOException {
        Airline airline = new Airline("AC", "ACA", "Air Canada");
        Flight flight = new Flight("NORM01", "AC014", "ACA014", airline,
                new Airport("YYZ", "CYYZ", "Toronto"),
                new Airport("YVR", "CYVR", "Vancouver"),
                new Date(), FlightStatus.SCHEDULED, null);

        ExportFlightsInputData input = new ExportFlightsInputData(
                List.of(flight), "CSV", false);
        interactor.execute(input);

        Path filePath = Paths.get(presenter.successData.getFilePath());
        String content = Files.readString(filePath);
        // Normal value should NOT be quoted
        assertTrue(content.contains("Air Canada") && !content.contains("\"Air Canada\""));
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("All FlightStatus enum values should be handled")
    void testAllFlightStatusValues() throws IOException {
        FlightStatus[] statuses = {FlightStatus.SCHEDULED, FlightStatus.DELAYED,
                FlightStatus.CANCELLED, FlightStatus.IN_AIR, FlightStatus.LANDED};

        for (FlightStatus status : statuses) {
            presenter.reset();
            Flight flight = new Flight("TEST" + status.name(), "AC" + status.name(),
                    "ACA" + status.name(), new Airline("AC", "ACA", "Air Canada"),
                    new Airport("YYZ", "CYYZ", "Toronto"),
                    new Airport("YVR", "CYVR", "Vancouver"),
                    new Date(), status, null);

            ExportFlightsInputData input = new ExportFlightsInputData(
                    List.of(flight), "CSV", false);
            interactor.execute(input);

            Path filePath = Paths.get(presenter.successData.getFilePath());
            String content = Files.readString(filePath);
            assertTrue(content.contains(status.getDisplayName()));
            Files.deleteIfExists(filePath);
        }
    }

    @Test
    @DisplayName("generateFileName should create unique timestamped filenames")
    void testGenerateFileName() throws IOException, InterruptedException {
        Flight flight1 = createFlight();
        ExportFlightsInputData input1 = new ExportFlightsInputData(
                List.of(flight1), "CSV", false);

        interactor.execute(input1);
        String fileName1 = presenter.successData.getFileName();

        assertTrue(fileName1.startsWith("flights_export_"));
        assertTrue(fileName1.endsWith(".csv"));

        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
        presenter.reset();

        Thread.sleep(1100);

        interactor.execute(input1);
        String fileName2 = presenter.successData.getFileName();

        assertNotEquals(fileName1, fileName2);
        Files.deleteIfExists(Paths.get(presenter.successData.getFilePath()));
    }
}
