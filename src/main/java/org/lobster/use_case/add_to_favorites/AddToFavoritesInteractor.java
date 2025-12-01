package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.util.Logger;
import java.util.regex.Pattern;

public class AddToFavoritesInteractor implements AddToFavoritesInputBoundary {
    private static final String CLASS_NAME = "AddToFavoritesInteractor";
    private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("[A-Z]{2}\\d+");

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
        String flightNumber = validateAndNormalizeInput(inputData);
        if (flightNumber == null) return;

        try {
            Flight flight = retrieveFlight(flightNumber);
            if (flight == null) {
                outputBoundary.prepareFailView("No flight found for " + flightNumber);  // CHANGED
                return;
            }

            if (checkForDuplicates(flightNumber)) {
                outputBoundary.prepareFailView("Flight " + flightNumber + " is already in favorites");
                return;
            }

            saveFlightToFavorites(flight);
            prepareSuccessResponse(flight);

        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Exception occurred while adding to favorites", e);
            outputBoundary.prepareFailView("Failed to add favorite: " + e.getMessage());
        }
    }

    private String validateAndNormalizeInput(AddToFavoritesInputData inputData) {
        if (inputData.getFlightNumber() == null) {
            outputBoundary.prepareFailView("Flight number is required");
            return null;
        }

        String flightNumber = inputData.getFlightNumber().trim().toUpperCase();

        if (flightNumber.isEmpty()) {
            outputBoundary.prepareFailView("Please enter a flight number/callsign");  // CHANGED
            return null;
        }

        if (!FLIGHT_NUMBER_PATTERN.matcher(flightNumber).matches()) {
            outputBoundary.prepareFailView("Invalid flight number format");
            return null;
        }

        return flightNumber;
    }

    private Flight retrieveFlight(String flightNumber) {
        Logger.getInstance().debug(CLASS_NAME, "Looking for flight number: '" + flightNumber + "'");
        Flight flight = flightDataGateway.findByFlightNumber(flightNumber);
        Logger.getInstance().debug(CLASS_NAME, "Found flight: " + (flight != null ?
                "Number: '" + flight.getFlightNumber() + "'" : "null"));
        return flight;
    }

    private boolean checkForDuplicates(String flightNumber) {
        boolean isDuplicate = favoritesDAO.existsByFlightNumber(flightNumber);
        Logger.getInstance().debug(CLASS_NAME, "Is duplicate: " + isDuplicate);
        return isDuplicate;
    }

    private void saveFlightToFavorites(Flight flight) {
        favoritesDAO.save(flight);
        Logger.getInstance().info(CLASS_NAME, "Flight saved to favorites: " + flight.getFlightNumber());
    }

    private void prepareSuccessResponse(Flight flight) {
        var updatedFavorites = favoritesDAO.findAll();
        Logger.getInstance().debug(CLASS_NAME, "Updated favorites count: " + updatedFavorites.size());

        AddToFavoritesOutputData outputData = new AddToFavoritesOutputData(
                true,
                "Flight " + flight.getFlightNumber() + " added to favorites!",
                flight,
                updatedFavorites
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}