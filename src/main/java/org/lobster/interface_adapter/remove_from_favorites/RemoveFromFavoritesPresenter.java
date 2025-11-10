package org.lobster.interface_adapter.remove_from_favorites;

import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesOutputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesOutputData;

public class RemoveFromFavoritesPresenter implements RemoveFromFavoritesOutputBoundary {
    private final FavoritesViewModel viewModel;

    public RemoveFromFavoritesPresenter(FavoritesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RemoveFromFavoritesOutputData outputData) {
        var state = new FavoritesViewModel.State(
                true,
                outputData.getMessage(),
                outputData.getRemovedFlight(),
                outputData.getUpdatedFavorites()
        );
        viewModel.setState(state);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        var state = new FavoritesViewModel.State(
                false,
                errorMessage,
                null,
                viewModel.getState().allFavorites // Keep existing favorites
        );
        viewModel.setState(state);
    }
}