package org.lobster.use_case.add_to_favorites;
/**
 * Output boundary for the Add to Favorites use case.
 */
public interface AddToFavoritesOutputBoundary {
    /**
     * Prepares the success view for the response.
     *
     * @param outputData the output data containing success information,
     *                   the added flight, and the updated favorites list
     */
    void prepareSuccessView(AddToFavoritesOutputData outputData);
    /**
     * Prepares the failure view for the response.
     *
     * @param errorMessage a descriptive message explaining the failure
     */
    void prepareFailView(String errorMessage);
}
