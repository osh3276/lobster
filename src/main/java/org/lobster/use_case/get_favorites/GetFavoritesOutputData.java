package org.lobster.use_case.get_favorites;

import org.lobster.entity.Flight;
import java.util.List;

public class GetFavoritesOutputData {
    private final boolean success;
    private final String message;
    private final List<Flight> favorites;

    public GetFavoritesOutputData(boolean success, String message, List<Flight> favorites) {
        this.success = success;
        this.message = message;
        this.favorites = favorites;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Flight> getFavorites() { return favorites; }
}