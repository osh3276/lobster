
package org.lobster.interface_adapter.get_favorites;

import org.lobster.use_case.get_favorites.GetFavoritesInputBoundary;

/**
 * Controller for the Get Favourites use case.
 */
public class GetFavoritesController {
    private final GetFavoritesInputBoundary getFavoritesUseCase;

    public GetFavoritesController(GetFavoritesInputBoundary getFavoritesUseCase) {
        this.getFavoritesUseCase = getFavoritesUseCase;
    }

    /**
     * Executes the Get Favorites use case by delegating to the interactor.
     */
    public void execute() {
        getFavoritesUseCase.execute();
    }
}
