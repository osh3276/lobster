package org.lobster.use_case.export_flights;

/**
 * Output data for the Export Flights use case.
 */
public class ExportFlightsOutputData {
    private final boolean success;
    private final String message;
    private final String filePath;
    private final String fileName;
    /**
     * Constructs a new {@code ExportFlightsOutputData} object.
     *
     * @param success  whether the export operation was successful
     * @param message  a message describing the result of the export
     * @param filePath the full path to the exported file
     * @param fileName the name of the exported file
     */
    public ExportFlightsOutputData(boolean success,
                                   String message,
                                   String filePath,
                                   String fileName) {
        this.success = success;
        this.message = message;
        this.filePath = filePath;
        this.fileName = fileName;
    }
    /**
     * Returns whether the export operation succeeded.
     *
     * @return {@code true} if the export was successful; {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * Returns the message describing the result of the export.
     *
     * @return a descriptive message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Returns the full path where the exported file was saved.
     *
     * @return the file path as a string
     */
    public String getFilePath() {
        return filePath;
    }
    /**
     * Returns the name of the exported file.
     *
     * @return the name of the file
     */
    public String getFileName() {
        return fileName;
    }
}
