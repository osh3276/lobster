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
        String flightNumber = inputData.getFlightNumber();
        System.out.println("DEBUG: AddToFavoritesInteractor executing for flight: " + flightNumber);

        try {
            // 1. Validate input
            if (flightNumber == null || flightNumber.trim().isEmpty()) {
                System.out.println("DEBUG: Flight number is empty");
                outputBoundary.prepareFailView("Flight number cannot be empty");
                return;
            }

            // 2. Check if flight exists in our system
            Flight flight = flightDataGateway.findByFlightNumber(flightNumber);
            System.out.println("DEBUG: Flight lookup result: " + (flight != null ? flight.getFlightNumber() : "null"));

            if (flight == null) {
                System.out.println("DEBUG: Flight not found");
                outputBoundary.prepareFailView("Flight not found: " + flightNumber);
                return;
            }

            // 3. Check for duplicates
            boolean isDuplicate = favoritesDAO.existsByFlightNumber(flightNumber);
            System.out.println("DEBUG: Is duplicate: " + isDuplicate);

            if (isDuplicate) {
                System.out.println("DEBUG: Flight is duplicate");
                outputBoundary.prepareFailView("Flight " + flightNumber + " is already in favorites");
                return;
            }

            // 4. Save to favorites
            favoritesDAO.save(flight);
            System.out.println("DEBUG: Flight saved to favorites");

            // 5. Get updated favorites list
            var updatedFavorites = favoritesDAO.findAll();
            System.out.println("DEBUG: Updated favorites count: " + updatedFavorites.size());

            // 6. Prepare success response
            AddToFavoritesOutputData outputData = new AddToFavoritesOutputData(
                    true,
                    "Flight " + flightNumber + " added to favorites!",
                    flight,
                    updatedFavorites
            );

            System.out.println("DEBUG: Calling outputBoundary.prepareSuccessView");
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            outputBoundary.prepareFailView("Failed to add favorite: " + e.getMessage());
        }
    }
}