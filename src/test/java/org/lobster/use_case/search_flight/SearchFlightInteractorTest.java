package org.lobster.use_case.search_flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lobster.entity.Flight;
import org.lobster.mocks.MockFlightDataGateway;
import org.lobster.mocks.TestHelper;
import org.lobster.interface_adapter.search_flight.SearchFlightPresenter;
import org.lobster.interface_adapter.search_flight.SearchFlightViewModel;

import static org.junit.jupiter.api.Assertions.*;

class SearchFlightInteractorTest {

    private SearchFlightInteractor interactor;
    private MockFlightDataGateway flightDataGateway;
    private SearchFlightViewModel viewModel;
    private SearchFlightPresenter presenter;

    @BeforeEach
    void setUp() {
        flightDataGateway = new MockFlightDataGateway();
        viewModel = new SearchFlightViewModel();
        presenter = new SearchFlightPresenter(viewModel);

        interactor = new SearchFlightInteractor(flightDataGateway, presenter);
    }

    @Test
    void execute_WhenFlightNumberIsEmpty_ShouldShowErrorMessage() {
        SearchFlightInputData inputData = new SearchFlightInputData("   ");
        interactor.execute(inputData);

        assertNull(viewModel.getFlight());
        assertEquals("Please enter a flight number", viewModel.getMessage());
    }

    @Test
    void execute_WhenFlightNotFound_ShouldShowNoFlightMessage() {
        String flightNumber = "AC123";
        SearchFlightInputData inputData = new SearchFlightInputData(flightNumber);

        flightDataGateway.setFlightToReturn(null);

        interactor.execute(inputData);

        assertNull(viewModel.getFlight());
        assertEquals("No flight found for AC123", viewModel.getMessage());
        assertEquals("AC123", flightDataGateway.getLastFlightNumberQueried());
    }

    @Test
    void execute_WhenFlightFound_ShouldPopulateViewModel() {
        String flightNumber = "AC123";
        SearchFlightInputData inputData = new SearchFlightInputData(" ac123 ");

        Flight testFlight = TestHelper.createTestFlight(flightNumber);
        flightDataGateway.setFlightToReturn(testFlight);

        interactor.execute(inputData);

        assertEquals(testFlight, viewModel.getFlight());
        assertEquals("Flight found: AC123", viewModel.getMessage());
        assertEquals("AC123", flightDataGateway.getLastFlightNumberQueried());
    }

    @Test
    void execute_WhenExceptionOccurs_ShouldShowErrorMessage() {
        SearchFlightInputData inputData = new SearchFlightInputData("AC999");

        flightDataGateway.setThrowException(true);

        interactor.execute(inputData);

        assertNull(viewModel.getFlight());
        assertNotNull(viewModel.getMessage());
        assertTrue(viewModel.getMessage().contains("Error finding flight"));
    }
}

