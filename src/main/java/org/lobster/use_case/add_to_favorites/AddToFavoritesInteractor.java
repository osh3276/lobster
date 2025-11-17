package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;

public class AddToFavoritesInteractor implements AddToFavoritesInputBoundary {

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
        String flightIdentifier = inputData.getFlightNumber().trim().toUpperCase();
        System.out.println("DEBUG: AddToFavoritesInteractor executing for: " + flightIdentifier);

        try {
            // 1. Validate input
            if (flightIdentifier.isEmpty()) {
                outputBoundary.prepareFailView("Please enter a flight number/callsign");
                return;
            }

            // 2. Check if flight exists using findByCallSign (like search does)
            Flight flight = flightDataGateway.findByCallSign(flightIdentifier);
            System.out.println("DEBUG: Looking for: '" + flightIdentifier + "'");
            System.out.println("DEBUG: Found flight: " + (flight != null ?
                    "Number: '" + flight.getFlightNumber() + "', Callsign: '" + flight.getCallsign() + "'" : "null"));

            if (flight == null) {
                outputBoundary.prepareFailView("No flight found for " + flightIdentifier);
                return;
            }

            // 3. Check for duplicates using the actual flight number from the found flight
            String actualFlightNumber = flight.getFlightNumber();
            boolean isDuplicate = favoritesDAO.existsByFlightNumber(actualFlightNumber);
            System.out.println("DEBUG: Is duplicate: " + isDuplicate);

            if (isDuplicate) {
                outputBoundary.prepareFailView("Flight " + actualFlightNumber + " is already in favorites");
                return;
            }

            // 4. Save to favorites
            favoritesDAO.save(flight);
            System.out.println("DEBUG: Flight saved to favorites: " + actualFlightNumber);

            // 5. Get updated favorites list
            var updatedFavorites = favoritesDAO.findAll();
            System.out.println("DEBUG: Updated favorites count: " + updatedFavorites.size());

            // 6. Prepare success response
            AddToFavoritesOutputData outputData = new AddToFavoritesOutputData(
                    true,
                    "Flight " + actualFlightNumber + " added to favorites!",
                    flight,
                    updatedFavorites
            );

            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            outputBoundary.prepareFailView("Failed to add favorite: " + e.getMessage());
        }
    }
}