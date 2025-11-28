package org.lobster.interface_adapter.search_flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lobster.entity.Flight;
import org.lobster.mocks.MockSearchFlightViewModel;
import org.lobster.use_case.search_flight.SearchFlightOutputData;

import static org.junit.jupiter.api.Assertions.*;

class SearchFlightPresenterTest {

    private SearchFlightPresenter presenter;
    private MockSearchFlightViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new MockSearchFlightViewModel();
        presenter = new SearchFlightPresenter(viewModel);
    }

    @Test
    void present_ShouldUpdateViewModelWithFlightAndMessage() {
        // Arrange
        Flight f = new Flight("HEX", "AC123", "CALLSIGN", null, null, null, null, null, null);
        SearchFlightOutputData output = new SearchFlightOutputData(f, "Flight found: AC123");

        // Act
        presenter.present(output);

        // Assert
        assertEquals(f, viewModel.lastFlight);
        assertEquals("Flight found: AC123", viewModel.lastMessage);
        assertEquals(1, viewModel.flightSetCount);
        assertEquals(1, viewModel.messageSetCount);
    }

    @Test
    void present_WhenNullFlight_ShouldStillSetMessage() {
        // Arrange
        SearchFlightOutputData output = new SearchFlightOutputData(null, "No flight found");

        // Act
        presenter.present(output);

        // Assert
        assertNull(viewModel.lastFlight);
        assertEquals("No flight found", viewModel.lastMessage);
        assertEquals(1, viewModel.flightSetCount);
        assertEquals(1, viewModel.messageSetCount);
    }
}
