package org.lobster.use_case.get_favorites;
/**
 * Output boundary for the Get Favorites use case.
 */
public interface GetFavoritesOutputBoundary {
    /**
     * Prepares the success view when the favorite flights are successfully
     * retrieved.
     *
     * @param outputData the output data containing the retrieved favorites and
     *                   a result message
     */
    void prepareSuccessView(GetFavoritesOutputData outputData);
    /**
     * Prepares the failure view when the favorites retrieval operation fails.
     *
     * @param errorMessage a message describing the reason for failure
     */
    void prepareFailView(String errorMessage);
}
