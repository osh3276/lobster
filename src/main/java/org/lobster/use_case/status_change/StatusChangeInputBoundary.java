package org.lobster.use_case.status_change;

/**
 * Input boundary for the Status Change use case.
 */
public interface StatusChangeInputBoundary {
    /**
     * Executes the Status Change use case.
     *
     * @param inputData the input data required for the use case
     */
    void execute(StatusChangeInputData inputData);
}
