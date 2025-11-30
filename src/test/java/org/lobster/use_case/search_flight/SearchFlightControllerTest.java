package org.lobster.use_case.search_flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lobster.interface_adapter.search_flight.SearchFlightController;
import org.lobster.mocks.MockSearchFlightInputBoundary;
import org.lobster.use_case.search_flight.SearchFlightInputData;

import static org.junit.jupiter.api.Assertions.*;

class SearchFlightControllerTest {

    private SearchFlightController controller;
    private MockSearchFlightInputBoundary interactor;

    @BeforeEach
    void setUp() {
        interactor = new MockSearchFlightInputBoundary();
        controller = new SearchFlightController(interactor);
    }

    @Test
    void onSearch_ShouldCallInteractorWithCorrectInput() {
        // Arrange
        String input = " AC213 ";

        // Act
        controller.onSearch(input);

        // Assert
        assertEquals(1, interactor.callCount);
        assertNotNull(interactor.lastInput);
        assertEquals(" AC213 ", interactor.lastInput.getFlightNumber());
        // controller should not trim or uppercase — interactor handles that
    }

    @Test
    void onSearch_WhenEmpty_ShouldStillCallInteractor() {
        // Arrange
        String input = "";

        // Act
        controller.onSearch(input);

        // Assert
        assertEquals(1, interactor.callCount);
        assertEquals("", interactor.lastInput.getFlightNumber());
    }
}

