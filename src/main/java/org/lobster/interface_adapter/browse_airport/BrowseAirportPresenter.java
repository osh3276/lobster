package org.lobster.interface_adapter.browse_airport;

import org.lobster.use_case.browse_airport.BrowseAirportOutputBoundary;
import org.lobster.use_case.browse_airport.BrowseAirportOutputData;

public class BrowseAirportPresenter implements BrowseAirportOutputBoundary {
    private final BrowseAirportViewModel viewModel;

    public BrowseAirportPresenter(BrowseAirportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(BrowseAirportOutputData data) {
        viewModel.setAirport(data.getAirport());
        viewModel.setMessage(data.getMessage());
    }
}
