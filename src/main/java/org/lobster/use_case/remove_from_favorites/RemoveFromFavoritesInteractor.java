package org.lobster.use_case.remove_from_favorites;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;

/**
 * Interactor for the Remove From Favorites use case.
 */
public class RemoveFromFavoritesInteractor implements RemoveFromFavoritesInputBoundary {

    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final FlightDataAccessInterface flightDataGateway;
    private final RemoveFromFavoritesOutputBoundary outputBoundary;
    /**
     * Constructs a new {@code RemoveFromFavoritesInteractor}.
     *
     * @param favoritesDAO     the data access interface for favorite flights
     * @param flightDataGateway the flight data provider used to retrieve
     *                          flight details
     * @param outputBoundary   the output boundary used to format the response
     */
    public RemoveFromFavoritesInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                         FlightDataAccessInterface flightDataGateway,
                                         RemoveFromFavoritesOutputBoundary outputBoundary) {
        this.favoritesDAO = favoritesDAO;
        this.flightDataGateway = flightDataGateway;
        this.outputBoundary = outputBoundary;
    }
    /**
     * Executes the Remove From Favorites use case.
     *
     * <p>The method performs the following operations:
     * <ol>
     *     <li>Validates that the flight number is not empty.</li>
     *     <li>Checks whether the flight exists in the favorites list.</li>
     *     <li>Retrieves the full flight details before removal.</li>
     *     <li>Deletes the flight from the favorites list.</li>
     *     <li>Fetches the updated list of favorites.</li>
     *     <li>Returns the result through the output boundary.</li>
     * </ol>
     *
     * <p>Any unexpected exceptions are caught and passed to the output boundary
     * as a failure message.
     *
     * @param inputData the input data containing the flight number to remove
     */
    @Override
    public void execute(RemoveFromFavoritesInputData inputData) {
        String flightNumber = inputData.getFlightNumber();

        try {
            // 1. Validate input
            if (flightNumber == null || flightNumber.trim().isEmpty()) {
                outputBoundary.prepareFailView("Flight number cannot be empty");
                return;
            }

            // 2. Check if flight exists in favorites
            if (!favoritesDAO.existsByFlightNumber(flightNumber)) {
                outputBoundary.prepareFailView("Flight "
                        + flightNumber
                        + " is not in favorites");
                return;
            }

            // 3. Get flight details before removing (for the response)
            Flight flight = flightDataGateway.findByFlightNumber(flightNumber);
            if (flight == null) {
                // This shouldn't happen if it exists in favorites, but handle gracefully
                outputBoundary.prepareFailView("Flight data not found for: " + flightNumber);
                return;
            }

            // 4. Remove from favorites
            favoritesDAO.deleteByFlightNumber(flightNumber);

            // 5. Get updated favorites list
            var updatedFavorites = favoritesDAO.findAll();

            // 6. Prepare success response
            RemoveFromFavoritesOutputData outputData = new RemoveFromFavoritesOutputData(
                    true,
                    "Flight " + flightNumber + " removed from favorites!",
                    flight,
                    updatedFavorites
            );
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to remove favorite: " + e.getMessage());
        }
    }
}
