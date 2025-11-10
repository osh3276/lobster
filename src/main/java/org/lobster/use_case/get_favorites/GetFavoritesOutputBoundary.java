package org.lobster.use_case.get_favorites;

public interface GetFavoritesOutputBoundary {
    void prepareSuccessView(GetFavoritesOutputData outputData);
    void prepareFailView(String errorMessage);
}
