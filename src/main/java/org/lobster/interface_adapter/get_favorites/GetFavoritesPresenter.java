package org.lobster.interface_adapter.get_favorites;

import org.lobster.use_case.get_favorites.GetFavoritesOutputBoundary;
import org.lobster.use_case.get_favorites.GetFavoritesOutputData;
import org.lobster.interface_adapter.FavoritesViewModel;

public class GetFavoritesPresenter implements GetFavoritesOutputBoundary {
    private final FavoritesViewModel viewModel;

    public GetFavoritesPresenter(FavoritesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GetFavoritesOutputData outputData) {
        var state = new FavoritesViewModel.State(
                true,
                outputData.getMessage(),
                null,
                outputData.getFavorites()
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