package org.lobster.use_case.get_favorites;

import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.util.Logger;
import org.lobster.exception.FavoritesException;
import org.lobster.use_case.get_favorites.GetFavoritesOutputBoundary;
/**
 * Interactor for the Get Favorites use case.
 */
public class GetFavoritesInteractor implements GetFavoritesInputBoundary {
    private static final String CLASS_NAME = GetFavoritesInteractor.class.getSimpleName();
    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final GetFavoritesOutputBoundary outputBoundary;
    /**
     * Constructs a new {@code GetFavoritesInteractor}.
     *
     * @param favoritesDAO    the data access interface used to retrieve
     *                        favorite flights
     * @param outputBoundary  the output boundary responsible for presenting
     *                        the retrieved favorites or an error message
     */
    public GetFavoritesInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                  GetFavoritesOutputBoundary outputBoundary) {
        this.favoritesDAO = favoritesDAO;
        this.outputBoundary = outputBoundary;
    }
    /**
     * Executes the Get Favorites use case.
     *
     * <p>The method performs the following steps:
     * <ol>
     *     <li>Retrieves the list of favorite flights from the DAO.</li>
     *     <li>Wraps the retrieved data inside a GetFavoritesOutputData object.</li>
     *     <li>Passes the data to the presenter using the output boundary.</li>
     *     <li>Handles and reports unexpected errors.</li>
     * </ol>
     */
    @Override
    public void execute() {
        try {
            var favorites = favoritesDAO.findAll();
            var outputData = new GetFavoritesOutputData(true,
                    "Favorites loaded",
                    favorites);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME,
                    "Failed to get favorites",
                    e);
            outputBoundary.prepareFailView("Failed to retrieve favorites: "
                    + e.getMessage());
        }
    }
}