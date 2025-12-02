package org.lobster.use_case.get_favorites;

import org.lobster.entity.Flight;
import java.util.List;

/**
 * Output data for the Get Favorites use case.
 */
public class GetFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final List<Flight> favorites;
    /**
     * Constructs a new GetFavoritesOutputData object.
     *
     * @param success   whether the favorites retrieval succeeded
     * @param message   a user-facing message describing the result
     * @param favorites the list of retrieved favorite flights; may be empty
     */
    public GetFavoritesOutputData(boolean success,
                                  String message,
                                  List<Flight> favorites) {
        this.success = success;
        this.message = message;
        this.favorites = favorites;
    }
    /**
     * Returns whether the favorites retrieval operation succeeded.
     *
     * @return true if successful; false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * Returns the message describing the result of the retrieval.
     *
     * @return a descriptive message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Returns the list of favorite flights.
     *
     * @return a list of Flight objects representing user favorites
     */
    public List<Flight> getFavorites() {
        return favorites;
    }
}