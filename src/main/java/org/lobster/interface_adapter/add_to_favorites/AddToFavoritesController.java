package org.lobster.interface_adapter.add_to_favorites;

import org.lobster.use_case.add_to_favorites.AddToFavoritesInputBoundary;
import org.lobster.use_case.add_to_favorites.AddToFavoritesInputData;

/**
 * Controller for the Add To Favourites Use Case.
 */
public class AddToFavoritesController {
    private final AddToFavoritesInputBoundary addToFavoritesUseCase;

    public AddToFavoritesController(AddToFavoritesInputBoundary addToFavoritesUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
    }

    /**
     * Execute block for the controller.
     * @param flightNumber "inputted flight number".
     */
    public void execute(String flightNumber) {
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        addToFavoritesUseCase.execute(inputData);
    }
}
