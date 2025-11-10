package org.lobster.use_case.remove_from_favorites;

import org.lobster.entity.Flight;
import java.util.List;

public class RemoveFromFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final Flight removedFlight;
    private final List<Flight> updatedFavorites;

    public RemoveFromFavoritesOutputData(boolean success, String message, Flight removedFlight, List<Flight> updatedFavorites) {
        this.success = success;
        this.message = message;
        this.removedFlight = removedFlight;
        this.updatedFavorites = updatedFavorites;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Flight getRemovedFlight() { return removedFlight; }
    public List<Flight> getUpdatedFavorites() { return updatedFavorites; }
}