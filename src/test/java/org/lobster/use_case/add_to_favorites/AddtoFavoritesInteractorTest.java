package org.lobster.use_case.add_to_favorites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lobster.entity.Flight;
import org.lobster.mocks.MockFavoriteFlightsDAO;
import org.lobster.mocks.MockFlightDataGateway;
import org.lobster.mocks.MockOutputBoundaries;
import org.lobster.mocks.MockOutputBoundaries.MockAddToFavoritesOutputBoundary;
import org.lobster.mocks.TestHelper;

import static org.junit.jupiter.api.Assertions.*;

class AddToFavoritesInteractorTest {

    private AddToFavoritesInteractor interactor;
    private MockFavoriteFlightsDAO favoritesDAO;
    private MockFlightDataGateway flightDataGateway;
    private MockAddToFavoritesOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        favoritesDAO = new MockFavoriteFlightsDAO();
        flightDataGateway = new MockFlightDataGateway();
        outputBoundary = new MockOutputBoundaries.MockAddToFavoritesOutputBoundary();
        interactor = new AddToFavoritesInteractor(favoritesDAO, flightDataGateway, outputBoundary);
    }

    @Test
    void execute_WhenFlightNumberIsNull_ShouldReturnEarly() {
        // Arrange
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(favoritesDAO.saveCalled);
        assertNull(outputBoundary.lastSuccessOutput);
        assertNull(outputBoundary.lastFailMessage);
    }

    @Test
    void execute_WhenFlightNumberIsEmpty_ShouldPrepareFailView() {
        // Arrange
        AddToFavoritesInputData inputData = new AddToFavoritesInputData("   ");

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals("Please enter a flight number/callsign", outputBoundary.lastFailMessage);
        assertFalse(favoritesDAO.saveCalled);
    }

    @Test
    void execute_WhenFlightNotFound_ShouldPrepareFailView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        flightDataGateway.setFlightToReturn(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals("No flight found for " + flightNumber, outputBoundary.lastFailMessage);
        assertFalse(favoritesDAO.saveCalled);
    }

    @Test
    void execute_WhenValidFlight_ShouldAddToFavoritesAndPrepareSuccessView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        Flight flight = TestHelper.createTestFlight(flightNumber);

        flightDataGateway.setFlightToReturn(flight);
        favoritesDAO.setExistsByFlightNumberResult(false);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(favoritesDAO.saveCalled);
        assertEquals(flight, favoritesDAO.lastSavedFlight);
        assertNotNull(outputBoundary.lastSuccessOutput);
        assertTrue(outputBoundary.lastSuccessOutput.isSuccess());
        assertEquals("Flight " + flightNumber + " added to favorites!", outputBoundary.lastSuccessOutput.getMessage());
        assertEquals(flight, outputBoundary.lastSuccessOutput.getFlight());
    }

    // ... other test methods using the same pattern
    @Test
    void execute_WhenFlightAlreadyInFavorites_ShouldPrepareFailView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        Flight flight = TestHelper.createTestFlight(flightNumber);

        flightDataGateway.setFlightToReturn(flight);
        favoritesDAO.setExistsByFlightNumberResult(true); // This makes isDuplicate = true

        // Act
        interactor.execute(inputData);

        // Assert - This covers the duplicate check block
        assertEquals("Flight " + flightNumber + " is already in favorites", outputBoundary.lastFailMessage);
        assertFalse(favoritesDAO.saveCalled);
        assertEquals(1, outputBoundary.failCallCount);
    }

    @Test
    void execute_WhenExceptionOccursInFlightLookup_ShouldCatchAndPrepareFailView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);

        // Make flightDataGateway throw an exception
        flightDataGateway.setThrowException(true);

        // Act
        interactor.execute(inputData);

        // Assert - This covers the exception catch block
        assertNotNull(outputBoundary.lastFailMessage);
        assertTrue(outputBoundary.lastFailMessage.contains("Failed to add favorite"));
        assertFalse(favoritesDAO.saveCalled);
        assertEquals(1, outputBoundary.failCallCount);
    }

    @Test
    void execute_WhenExceptionOccursInDuplicateCheck_ShouldCatchAndPrepareFailView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        Flight flight = TestHelper.createTestFlight(flightNumber);

        flightDataGateway.setFlightToReturn(flight);
        // Make favoritesDAO.existsByFlightNumber throw an exception
        favoritesDAO.setThrowExceptionOnExists(true);

        // Act
        interactor.execute(inputData);

        // Assert - This covers exception in duplicate check
        assertNotNull(outputBoundary.lastFailMessage);
        assertTrue(outputBoundary.lastFailMessage.contains("Failed to add favorite"));
        assertFalse(favoritesDAO.saveCalled);
    }

    @Test
    void execute_WhenExceptionOccursInSave_ShouldCatchAndPrepareFailView() {
        // Arrange
        String flightNumber = "AA123";
        AddToFavoritesInputData inputData = new AddToFavoritesInputData(flightNumber);
        Flight flight = TestHelper.createTestFlight(flightNumber);

        flightDataGateway.setFlightToReturn(flight);
        favoritesDAO.setExistsByFlightNumberResult(false); // Not a duplicate
        // Make favoritesDAO.save throw an exception
        favoritesDAO.setThrowExceptionOnSave(true);

        // Act
        interactor.execute(inputData);

        // Assert - This covers exception during save
        assertNotNull(outputBoundary.lastFailMessage);
        assertTrue(outputBoundary.lastFailMessage.contains("Failed to add favorite"));
        // Note: save was attempted but failed
    }
}