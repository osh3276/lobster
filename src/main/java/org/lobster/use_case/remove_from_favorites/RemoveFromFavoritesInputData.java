package org.lobster.use_case.remove_from_favorites;

/**
 * Input data for the Remove From Favorites use case.
 */
public class RemoveFromFavoritesInputData {
    private final String flightNumber;
    /**
     * Constructs a new {@code RemoveFromFavoritesInputData} instance.
     *
     * @param flightNumber the flight number to remove from the favorites list
     */
    public RemoveFromFavoritesInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    /**
     * Returns the flight number to be removed from favorites.
     *
     * @return the flight number as a string
     */
    public String getFlightNumber() {
        return flightNumber;
    }
}
