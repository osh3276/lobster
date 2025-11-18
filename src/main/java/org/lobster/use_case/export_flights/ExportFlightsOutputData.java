package org.lobster.use_case.export_flights;

public class ExportFlightsOutputData {
    private final boolean success;
    private final String message;
    private final String filePath;
    private final String fileName;

    public ExportFlightsOutputData(boolean success, String message, String filePath, String fileName) {
        this.success = success;
        this.message = message;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }
}

