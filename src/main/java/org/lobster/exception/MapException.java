
package org.lobster.exception;

/**
 * Exception thrown when map operations fail.
 */
public class MapException extends Exception {
    
    public MapException(String message) {

        super(message);
    }
    
    public MapException(String message, Throwable cause) {

        super(message, cause);
    }
}