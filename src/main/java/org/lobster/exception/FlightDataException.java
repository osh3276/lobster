package org.lobster.exception;

/**
 * Exception thrown when flight data cannot be retrieved from external APIs
 */
public class FlightDataException extends Exception {
    
    public FlightDataException(String message) {
        super(message);
    }
    
    public FlightDataException(String message, Throwable cause) {
        super(message, cause);
    }
}