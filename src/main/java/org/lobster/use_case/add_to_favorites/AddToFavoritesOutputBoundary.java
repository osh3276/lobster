package org.lobster.use_case.add_to_favorites;

public interface AddToFavoritesOutputBoundary {
    void prepareSuccessView(AddToFavoritesOutputData outputData);
    void prepareFailView(String errorMessage);
}