package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import java.util.List;

public class AddToFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final Flight flight;
    private final List<Flight> updatedFavorites;

    public AddToFavoritesOutputData(boolean success, String message, Flight flight, List<Flight> updatedFavorites) {
        this.success = success;
        this.message = message;
        this.flight = flight;
        this.updatedFavorites = updatedFavorites;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Flight getFlight() { return flight; }
    public List<Flight> getUpdatedFavorites() { return updatedFavorites; }
}