package org.lobster.use_case.add_to_favorites;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.util.Logger;
import java.util.regex.Pattern;
/**
 * Interactor for the Add to Favorites use case.
 */
public class AddToFavoritesInteractor implements AddToFavoritesInputBoundary {
    private static final String CLASS_NAME = "AddToFavoritesInteractor";
    /**
     * Pattern validating flight numbers (e.g., "AC123").
     */
    private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("[A-Z]{2}\\d+");

    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final FlightDataAccessInterface flightDataGateway;
    private final AddToFavoritesOutputBoundary outputBoundary;
    /**
     * Constructs the interactor with its required dependencies.
     *
     * @param favoritesDAO the data access object for storing favorite flights
     * @param flightDataGateway the gateway used to look up flight information
     * @param outputBoundary the presenter used to prepare user-facing output
     */
    public AddToFavoritesInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                    FlightDataAccessInterface flightDataGateway,
                                    AddToFavoritesOutputBoundary outputBoundary) {
        this.favoritesDAO = favoritesDAO;
        this.flightDataGateway = flightDataGateway;
        this.outputBoundary = outputBoundary;
    }
    /**
     * Executes the Add to Favorites use case.
     *
     * <p>The steps are:
     * <ol>
     *     <li>Validate and normalize the input flight number.</li>
     *     <li>Retrieve the flight from the flight data gateway.</li>
     *     <li>Check for duplicates in favorites.</li>
     *     <li>Save the flight to favorites.</li>
     *     <li>Return updated favorites through the presenter.</li>
     * </ol>
     *
     * @param inputData the request model containing the flight number
     */
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
    /**
     * Validates and normalizes the input flight number.
     *
     * @param inputData the input containing the raw flight number
     * @return the cleaned flight number or null if validation fails
     */
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
    /**
     * Retrieves a flight using the flight data gateway.
     *
     * @param flightNumber the normalized flight number
     * @return the corresponding {@link Flight} or null if not found
     */
    private Flight retrieveFlight(String flightNumber) {
        Logger.getInstance().debug(CLASS_NAME, "Looking for flight number: '" + flightNumber + "'");
        Flight flight = flightDataGateway.findByFlightNumber(flightNumber);
        Logger.getInstance().debug(CLASS_NAME, "Found flight: " + (flight != null ?
                "Number: '" + flight.getFlightNumber() + "'" : "null"));
        return flight;
    }
    /**
     * Checks whether the flight is already in favorites.
     *
     * @param flightNumber the flight number to check
     * @return true if a duplicate exists; false otherwise
     */
    private boolean checkForDuplicates(String flightNumber) {
        boolean isDuplicate = favoritesDAO.existsByFlightNumber(flightNumber);
        Logger.getInstance().debug(CLASS_NAME, "Is duplicate: " + isDuplicate);
        return isDuplicate;
    }
    /**
     * Saves the flight to the favorites data store.
     *
     * @param flight the flight to save
     */
    private void saveFlightToFavorites(Flight flight) {
        favoritesDAO.save(flight);
        Logger.getInstance().info(CLASS_NAME, "Flight saved to favorites: " + flight.getFlightNumber());
    }
    /**
     * Prepares a success response containing the updated favorites list.
     *
     * @param flight the newly added favorite flight
     */
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
