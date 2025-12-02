package org.lobster.exception;

/**
 * Exception thrown when favorites operations fail.
 */
public class FavoritesException extends Exception {
    
    public FavoritesException(String message) {
        super(message);
    }
    
    public FavoritesException(String message, Throwable cause) {
        super(message, cause);
    }
}
