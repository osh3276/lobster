package org.lobster.use_case.status_change;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lobster.entity.Flight;
import org.lobster.entity.FlightStatus;
import org.lobster.mocks.MockFavoriteFlightsDAO;
import org.lobster.mocks.MockFlightDataGateway;
import org.lobster.mocks.MockStatusChangeOutputBoundary;
import org.lobster.mocks.TestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusChangeInteractorTest {

    private StatusChangeInteractor interactor;
    private MockFavoriteFlightsDAO favoritesDAO;
    private MockFlightDataGateway flightAPI;
    private MockStatusChangeOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        favoritesDAO = new MockFavoriteFlightsDAO();
        flightAPI = new MockFlightDataGateway();
        outputBoundary = new MockStatusChangeOutputBoundary();

        interactor = new StatusChangeInteractor(
                favoritesDAO,
                flightAPI,
                outputBoundary
        );
    }

    @Test
    void execute_WhenNoFavorites_ShouldDoNothing() {
        StatusChangeInputData input = new StatusChangeInputData();

        interactor.execute(input);

        assertEquals(0, outputBoundary.callCount);
        assertFalse(favoritesDAO.saveCalled);
    }

    @Test
    void execute_WhenStatusUnchanged_ShouldNotTriggerPresenter() {
        Flight initial = TestHelper.createTestFlight("AA100", FlightStatus.IN_AIR);
        favoritesDAO.setFavoritesList(List.of(initial));

        // Mock API returning same status
        flightAPI.setFlightToReturn(TestHelper.createTestFlight("AA100", FlightStatus.IN_AIR));

        interactor.execute(new StatusChangeInputData());

        assertEquals(0, outputBoundary.callCount);
        assertFalse(favoritesDAO.saveCalled);
    }

    @Test
    void execute_WhenStatusChanges_ShouldSaveAndNotify() {
        Flight initial = TestHelper.createTestFlight("AA100", FlightStatus.SCHEDULED);
        favoritesDAO.setFavoritesList(List.of(initial));

        // API returns a new flight with different status
        Flight updated = TestHelper.createTestFlight("AA100", FlightStatus.SCHEDULED);
        flightAPI.setFlightToReturn(updated);

        interactor.execute(new StatusChangeInputData());

        assertTrue(favoritesDAO.saveCalled);
        assertEquals(updated, favoritesDAO.lastSavedFlight);
        assertEquals(1, outputBoundary.callCount);
        assertEquals(updated, outputBoundary.lastOutput.getUpdatedFlight());
    }

    @Test
    void execute_WhenFlightLookupFails_ShouldNotCrash() {
        Flight initial = TestHelper.createTestFlight("AA100", FlightStatus.SCHEDULED);
        favoritesDAO.setFavoritesList(List.of(initial));

        flightAPI.setThrowException(true);

        interactor.execute(new StatusChangeInputData());

        // Should gracefully skip
        assertFalse(favoritesDAO.saveCalled);
        assertEquals(0, outputBoundary.callCount);
    }
}