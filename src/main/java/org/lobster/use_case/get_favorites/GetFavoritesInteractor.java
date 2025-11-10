package org.lobster.use_case.get_favorites;

import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.use_case.get_favorites.GetFavoritesOutputBoundary;

public class GetFavoritesInteractor implements GetFavoritesInputBoundary {
    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final GetFavoritesOutputBoundary outputBoundary;

    public GetFavoritesInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                  GetFavoritesOutputBoundary outputBoundary) {
        this.favoritesDAO = favoritesDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute() {
        try {
            var favorites = favoritesDAO.findAll();
            var outputData = new GetFavoritesOutputData(true, "Favorites loaded", favorites);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to load favorites: " + e.getMessage());
        }
    }
}