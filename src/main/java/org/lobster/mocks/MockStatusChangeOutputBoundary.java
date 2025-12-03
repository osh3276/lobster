package org.lobster.mocks;

import org.lobster.use_case.status_change.StatusChangeOutputBoundary;
import org.lobster.use_case.status_change.StatusChangeOutputData;

public class MockStatusChangeOutputBoundary implements StatusChangeOutputBoundary {

    public int callCount = 0;
    public StatusChangeOutputData lastOutput;

    @Override
    public void present(StatusChangeOutputData outputData) {
        callCount++;
        lastOutput = outputData;
    }
}