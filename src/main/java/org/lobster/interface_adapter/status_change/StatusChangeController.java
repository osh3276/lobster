package org.lobster.interface_adapter.status_change;

import org.lobster.use_case.status_change.*;

public class StatusChangeController {
    private final StatusChangeInputBoundary interactor;

    public StatusChangeController(StatusChangeInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Status Change use case by requesting a poll for updated flight statuses.
     *
     */
    public void poll() {
        interactor.execute(new StatusChangeInputData());
    }
}
