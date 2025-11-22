package org.lobster.use_case.get_favorites;

import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.util.Logger;
import org.lobster.exception.FavoritesException;
import org.lobster.use_case.get_favorites.GetFavoritesOutputBoundary;

public class GetFavoritesInteractor implements GetFavoritesInputBoundary {
    private static final String CLASS_NAME = GetFavoritesInteractor.class.getSimpleName();
    
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
            Logger.getInstance().error(CLASS_NAME, "Failed to get favorites", e);
            outputBoundary.prepareFailView("Failed to retrieve favorites: " + e.getMessage());
        }
    }
}