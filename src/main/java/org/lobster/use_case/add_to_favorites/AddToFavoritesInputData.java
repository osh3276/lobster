package org.lobster.use_case.add_to_favorites;

/**
 * Input Data for adding a flight to favorites.
 */
public class AddToFavoritesInputData {
    private final String flightNumber;
    /**
     * Constructs the input data for adding a flight to favorites.
     *
     * @param flightNumber the flight number entered by the user;
     *                     may be unformatted or contain whitespace
     */
    public AddToFavoritesInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    /**
     * Returns the raw flight number provided by the user.
     *
     * @return the flight number string
     */
    public String getFlightNumber() {
        return flightNumber;
    }
}
