package org.lobster.use_case.map_view;

/**
 * Input boundary for map update use case.
 */
public interface UpdateMapInputBoundary {
    /**
     * Executes the Update Map use case using the provided input data.
     *
     * @param inputData the input data containing flight numbers and map
     *                  dimensions needed to perform the update
     */
    void execute(UpdateMapInputData inputData);
}
