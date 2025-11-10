package org.lobster.interface_adapter.get_favorites;

import org.lobster.use_case.get_favorites.GetFavoritesInputBoundary;

public class GetFavoritesController {
    private final GetFavoritesInputBoundary getFavoritesUseCase;

    public GetFavoritesController(GetFavoritesInputBoundary getFavoritesUseCase) {
        this.getFavoritesUseCase = getFavoritesUseCase;
    }

    public void execute() {
        getFavoritesUseCase.execute();
    }
}