package org.lobster.use_case.add_to_favorites;


/**
 * Input Data for adding a flight to favorites.
 */
public class AddToFavoritesInputData {
    private final String flightNumber;

    public AddToFavoritesInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}