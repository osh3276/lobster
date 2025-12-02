package org.lobster.use_case.browse_airport;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lobster.entity.Airport;
import org.lobster.mocks.MockFlightRadarService;
import org.lobster.interface_adapter.browse_airport.BrowseAirportPresenter;
import org.lobster.interface_adapter.browse_airport.BrowseAirportViewModel;

import static org.junit.jupiter.api.Assertions.*;

class BrowseAirportInteractorTest {

    private MockFlightRadarService mockService;
    private BrowseAirportViewModel viewModel;
    private BrowseAirportPresenter presenter;
    private BrowseAirportInteractor interactor;

    @BeforeEach
    void setUp() {
        mockService = new MockFlightRadarService();
        viewModel = new BrowseAirportViewModel();
        presenter = new BrowseAirportPresenter(viewModel);
        interactor = new BrowseAirportInteractor(mockService, presenter);
    }

    @Test
    void execute_WhenCodeIsEmpty_ShouldShowErrorMessage() {
        BrowseAirportInputData input = new BrowseAirportInputData("");

        interactor.execute(input);

        assertNull(viewModel.getAirport());
        assertEquals("Please enter an airport code.", viewModel.getMessage());
    }

    @Test
    void execute_WhenAirportNotFound_ShouldShowNoInfoMessage() {
        BrowseAirportInputData input = new BrowseAirportInputData("zzz");

        mockService.setAirportToReturn(null);

        interactor.execute(input);

        assertNull(viewModel.getAirport());
        assertEquals("No information found for: ZZZ", viewModel.getMessage());
        assertEquals("ZZZ", mockService.getLastAirportCodeQueried());
    }

    @Test
    void execute_WhenAirportFound_ShouldPopulateViewModel() {
        JSONObject json = new JSONObject();
        json.put("iata", "YYZ");
        json.put("icao", "CYYZ");
        json.put("name", "Toronto Pearson");

        mockService.setAirportToReturn(json);

        BrowseAirportInputData input = new BrowseAirportInputData("yyz");

        interactor.execute(input);

        Airport airport = viewModel.getAirport();
        assertNotNull(airport);
        assertEquals("YYZ", airport.getIata());
        assertEquals("CYYZ", airport.getIcao());
        assertEquals("Toronto Pearson", airport.getName());

        assertEquals("", viewModel.getMessage());
    }

    @Test
    void execute_WhenExceptionThrown_ShouldShowNoInfoMessage() {
        BrowseAirportInputData input = new BrowseAirportInputData("abc");

        mockService.setThrowException(true);

        interactor.execute(input);

        assertNull(viewModel.getAirport());
        assertEquals("No information found for: ABC", viewModel.getMessage());
    }
}