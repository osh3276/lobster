
package org.lobster.interface_adapter.remove_from_favorites;

import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInputData;

/**
 * Controller for Remove from Favourites Use Case.
 */
public class RemoveFromFavoritesController {
    private final RemoveFromFavoritesInputBoundary removeFromFavoritesUseCase;

    public RemoveFromFavoritesController(RemoveFromFavoritesInputBoundary removeFromFavoritesUseCase) {
        this.removeFromFavoritesUseCase = removeFromFavoritesUseCase;
    }

    /**
     * Executes the Remove From Favorites use case by delegating to the interactor with the specified flight number.
     *
     * @param flightNumber "the identifier of the flight to remove from favorites"
     */
    public void execute(String flightNumber) {
        RemoveFromFavoritesInputData inputData = new RemoveFromFavoritesInputData(flightNumber);
        removeFromFavoritesUseCase.execute(inputData);
    }
}
