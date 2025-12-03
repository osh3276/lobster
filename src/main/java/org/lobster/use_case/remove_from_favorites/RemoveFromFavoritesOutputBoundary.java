package org.lobster.use_case.remove_from_favorites;

/**
 * Output boundary for the Remove From Favorites use case.
 */
public interface RemoveFromFavoritesOutputBoundary {
    /**
     * Prepares the success view for the UI when a flight is successfully
     * removed from the favorites list.
     *
     * @param outputData the output data containing removal details and the
     *                   updated favorites list
     */
    void prepareSuccessView(RemoveFromFavoritesOutputData outputData);
    /**
     * Prepares the failure view for the UI when the removal operation fails.
     *
     * @param errorMessage the message describing why the removal failed
     */
    void prepareFailView(String errorMessage);
}
