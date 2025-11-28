package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.util.Logger;
import org.lobster.exception.FavoritesException;

public class AddToFavoritesInteractor implements AddToFavoritesInputBoundary {

    private static final String CLASS_NAME = "AddToFavoritesInteractor";
    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final FlightDataAccessInterface flightDataGateway;
    private final AddToFavoritesOutputBoundary outputBoundary;

    public AddToFavoritesInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                    FlightDataAccessInterface flightDataGateway,
                                    AddToFavoritesOutputBoundary outputBoundary) {
        this.favoritesDAO = favoritesDAO;
        this.flightDataGateway = flightDataGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(AddToFavoritesInputData inputData) {
        // Instead of silent return on null:
        if (inputData.getFlightNumber() == null) {
            outputBoundary.prepareFailView("Flight number is required");
            return;
        }


        String flightNumber = inputData.getFlightNumber().trim().toUpperCase();
        Logger.getInstance().debug(CLASS_NAME, "executing for flight number: " + flightNumber);

        if (!flightNumber.matches("[A-Z]{2}\\d+")) {
            outputBoundary.prepareFailView("Invalid flight number format");
            return;
        }

        try {
            // 1. Validate input
            if (flightNumber.isEmpty()) {
                outputBoundary.prepareFailView("Please enter a flight number");
                return;
            }

            // 2. Check if flight exists using flight number directly
            Flight flight = flightDataGateway.findByFlightNumber(flightNumber);
            Logger.getInstance().debug(CLASS_NAME, "Looking for flight number: '" + flightNumber + "'");
            Logger.getInstance().debug(CLASS_NAME, "Found flight: " + (flight != null ?
                    "Number: '" + flight.getFlightNumber() + "'" : "null"));

            if (flight == null) {
                outputBoundary.prepareFailView("No flight found for flight number " + flightNumber);
                return;
            }

            // 3. Check for duplicates using the flight number
            boolean isDuplicate = favoritesDAO.existsByFlightNumber(flightNumber);
            Logger.getInstance().debug(CLASS_NAME, "Is duplicate: " + isDuplicate);

            if (isDuplicate) {
                outputBoundary.prepareFailView("Flight " + flightNumber + " is already in favorites");
                return;
            }

            // 4. Save to favorites
            favoritesDAO.save(flight);
            Logger.getInstance().info(CLASS_NAME, "Flight saved to favorites: " + flightNumber);

            // 5. Get updated favorites list
            var updatedFavorites = favoritesDAO.findAll();
            Logger.getInstance().debug(CLASS_NAME, "Updated favorites count: " + updatedFavorites.size());

            // 6. Prepare success response
            AddToFavoritesOutputData outputData = new AddToFavoritesOutputData(
                    true,
                    "Flight " + flightNumber + " added to favorites!",
                    flight,
                    updatedFavorites
            );

            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Exception occurred while adding to favorites", e);
            outputBoundary.prepareFailView("Failed to add favorite: " + e.getMessage());
        }
    }
}