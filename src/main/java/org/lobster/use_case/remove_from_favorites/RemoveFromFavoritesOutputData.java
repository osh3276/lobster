package org.lobster.use_case.remove_from_favorites;

import org.lobster.entity.Flight;
import java.util.List;

/**
 * Output data for the Remove From Favorites use case.
 */
public class RemoveFromFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final Flight removedFlight;
    private final List<Flight> updatedFavorites;
    /**
     * Constructs a new RemoveFromFavoritesOutputData object.
     *
     * @param success          whether the removal operation succeeded
     * @param message          a message describing the result of the operation
     * @param removedFlight    the flight that was removed, or null if
     *                         the operation failed
     * @param updatedFavorites the updated list of all favorite flights
     */
    public RemoveFromFavoritesOutputData(boolean success,
                                         String message,
                                         Flight removedFlight,
                                         List<Flight> updatedFavorites) {
        this.success = success;
        this.message = message;
        this.removedFlight = removedFlight;
        this.updatedFavorites = updatedFavorites;
    }
    /**
     * Returns whether the removal operation succeeded.
     *
     * @return true if the flight was successfully removed;
     *         false otherwise
     */
    public boolean isSuccess() {
        return success; }
    /**
     * Returns the message describing the result of the operation.
     *
     * @return a user-facing result message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Returns the removed flight.
     *
     * @return the removed flight, or null if removal was not successful
     */
    public Flight getRemovedFlight() {
        return removedFlight;
    }
    /**
     * Returns the updated list of favorite flights.
     *
     * @return the list of remaining favorite flights
     */
    public List<Flight> getUpdatedFavorites() {
        return updatedFavorites;
    }
}
