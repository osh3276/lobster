package org.lobster.interface_adapter.status_change;

import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.use_case.status_change.StatusChangeOutputBoundary;
import org.lobster.use_case.status_change.StatusChangeOutputData;

public class StatusChangePresenter implements StatusChangeOutputBoundary {

    private final StatusChangeViewModel viewModel;

    public StatusChangePresenter(StatusChangeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(StatusChangeOutputData outputData) {
        viewModel.setLastUpdatedFlight(outputData.getUpdatdFlight());
        viewModel.notifyObservers();
    }
}
