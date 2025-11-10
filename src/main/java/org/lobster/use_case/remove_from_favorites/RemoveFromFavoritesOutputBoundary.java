package org.lobster.use_case.remove_from_favorites;

public interface RemoveFromFavoritesOutputBoundary {
    void prepareSuccessView(RemoveFromFavoritesOutputData outputData);
    void prepareFailView(String errorMessage);
}