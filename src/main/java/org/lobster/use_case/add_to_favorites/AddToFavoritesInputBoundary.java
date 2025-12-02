package org.lobster.use_case.add_to_favorites;
/**
 * Input boundary for the Add to Favorites use case.
 */
public interface AddToFavoritesInputBoundary {
    /**
     * Executes the Add to Favorites use case using the provided input data.
     *
     * @param inputData the request model containing the flight number
     */
    void execute(AddToFavoritesInputData inputData);
}
