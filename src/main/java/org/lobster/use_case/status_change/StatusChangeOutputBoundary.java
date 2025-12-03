package org.lobster.use_case.status_change;

/**
 * Output boundary for the Status Change use case.
 */
public interface StatusChangeOutputBoundary {
    /**
     * Presents the output data produced by the Status Change use case.
     *
     * @param outputData the data describing the updated flight and its status
     */
    void present(StatusChangeOutputData outputData);
}
