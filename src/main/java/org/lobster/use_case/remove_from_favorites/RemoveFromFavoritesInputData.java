package org.lobster.use_case.remove_from_favorites;

public class RemoveFromFavoritesInputData {
    private final String flightNumber;

    public RemoveFromFavoritesInputData(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}