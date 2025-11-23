package org.lobster.interface_adapter.add_to_favorites;

import org.lobster.use_case.add_to_favorites.AddToFavoritesOutputBoundary;
import org.lobster.use_case.add_to_favorites.AddToFavoritesOutputData;
import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.util.Logger;

public class AddToFavoritesPresenter implements AddToFavoritesOutputBoundary {
    private static final String CLASS_NAME = AddToFavoritesPresenter.class.getSimpleName();
    
    private final FavoritesViewModel viewModel;

    public AddToFavoritesPresenter(FavoritesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddToFavoritesOutputData outputData) {
        Logger.getInstance().debug(CLASS_NAME, "prepareSuccessView called");
        var state = new FavoritesViewModel.State(
                true,
                outputData.getMessage(),
                outputData.getFlight(),
                outputData.getUpdatedFavorites()
        );
        System.out.println("DEBUG: Presenter - setting new state with " + outputData.getUpdatedFavorites().size() + " favorites");
        viewModel.setState(state);
        System.out.println("DEBUG: Presenter - state set successfully");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("DEBUG: Presenter - prepareFailView called: " + errorMessage);
        var state = new FavoritesViewModel.State(
                false,
                errorMessage,
                null,
                viewModel.getState().allFavorites // Keep existing favorites
        );
        viewModel.setState(state);
    }
}