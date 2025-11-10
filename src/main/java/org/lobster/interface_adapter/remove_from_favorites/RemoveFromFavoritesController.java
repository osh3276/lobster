package org.lobster.interface_adapter.remove_from_favorites;

import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInputData;

public class RemoveFromFavoritesController {
    private final RemoveFromFavoritesInputBoundary removeFromFavoritesUseCase;

    public RemoveFromFavoritesController(RemoveFromFavoritesInputBoundary removeFromFavoritesUseCase) {
        this.removeFromFavoritesUseCase = removeFromFavoritesUseCase;
    }

    public void execute(String flightNumber) {
        RemoveFromFavoritesInputData inputData = new RemoveFromFavoritesInputData(flightNumber);
        removeFromFavoritesUseCase.execute(inputData);
    }
}