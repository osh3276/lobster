package org.lobster.interface_adapter.search_flight;

import org.lobster.use_case.search_flight.SearchFlightOutputBoundary;
import org.lobster.use_case.search_flight.SearchFlightOutputData;

public class SearchFlightPresenter implements SearchFlightOutputBoundary {

    private final SearchFlightViewModel viewModel;

    public SearchFlightPresenter(SearchFlightViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchFlightOutputData data) {
        viewModel.setFlight(data.getFlight());
        viewModel.setMessage(data.getMessage());
    }
}
