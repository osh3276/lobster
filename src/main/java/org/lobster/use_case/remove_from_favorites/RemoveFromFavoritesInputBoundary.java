package org.lobster.use_case.remove_from_favorites;

/**
 * Input boundary for the Remove From Favorites use case.
 */
public interface RemoveFromFavoritesInputBoundary {
    /**
     * Executes the Remove From Favorites use case using the provided input data.
     *
     * @param inputData the data object containing the flight number to remove
     */
    void execute(RemoveFromFavoritesInputData inputData);
}
