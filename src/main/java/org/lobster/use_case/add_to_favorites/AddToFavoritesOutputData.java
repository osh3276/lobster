package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import java.util.List;
/**
 * Output data for the Add to Favorites use case.
 */
public class AddToFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final Flight flight;
    private final List<Flight> updatedFavorites;
    /**
     * Constructs output data for the Add to Favorites use case.
     *
     * @param success whether the operation completed successfully
     * @param message a user-facing message describing the result
     * @param flight the flight that was added (or attempted to be added)
     * @param updatedFavorites the updated list of all favorite flights
     */
    public AddToFavoritesOutputData(boolean success,
                                    String message,
                                    Flight flight,
                                    List<Flight> updatedFavorites) {
        this.success = success;
        this.message = message;
        this.flight = flight;
        this.updatedFavorites = updatedFavorites;
    }
    /**
     * Returns whether the add-to-favorites operation succeeded.
     *
     * @return true if successful; false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * Returns the user-facing message describing the operation's outcome.
     *
     * @return a descriptive message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Returns the flight involved in the operation.
     *
     * @return the flight that was added or processed
     */
    public Flight getFlight() {
        return flight;
    }
    /**
     * Returns the updated list of favorite flights.
     *
     * @return the current list of favorites
     */
    public List<Flight> getUpdatedFavorites() {
        return updatedFavorites;
    }
}