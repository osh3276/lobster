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
        if (inputData.getFlightNumber() == null){
            return;
        }
        String flightIdentifier = inputData.getFlightNumber().trim().toUpperCase();
        Logger.getInstance().debug(CLASS_NAME, "executing for: " + flightIdentifier);

        try {
            // 1. Validate input
            if (flightIdentifier.isEmpty()) {
                outputBoundary.prepareFailView("Please enter a flight number/callsign");
                return;
            }

            // 2. Check if flight exists using findByCallSign (like search does)
            Flight flight = flightDataGateway.findByCallSign(flightIdentifier);
            Logger.getInstance().debug(CLASS_NAME, "Looking for: '" + flightIdentifier + "'");
            Logger.getInstance().debug(CLASS_NAME, "Found flight: " + (flight != null ?
                    "Number: '" + flight.getFlightNumber() + "', Callsign: '" + flight.getCallsign() + "'" : "null"));

            if (flight == null) {
                outputBoundary.prepareFailView("No flight found for " + flightIdentifier);
                return;
            }

            // 3. Check for duplicates using the actual flight number from the found flight
            boolean isDuplicate = favoritesDAO.existsByFlightNumber(flight.getFlightNumber());
            Logger.getInstance().debug(CLASS_NAME, "Is duplicate: " + isDuplicate);

            if (isDuplicate) {
                outputBoundary.prepareFailView("Flight " + flight.getFlightNumber() + " is already in favorites");
                return;
            }

            // 4. Save to favorites
            favoritesDAO.save(flight);
            Logger.getInstance().info(CLASS_NAME, "Flight saved to favorites: " + flight.getFlightNumber());

            // 5. Get updated favorites list
            var updatedFavorites = favoritesDAO.findAll();
            Logger.getInstance().debug(CLASS_NAME, "Updated favorites count: " + updatedFavorites.size());

            // 6. Prepare success response
            AddToFavoritesOutputData outputData = new AddToFavoritesOutputData(
                    true,
                    "Flight " + flight.getFlightNumber() + " added to favorites!",
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