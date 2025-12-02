package org.lobster.use_case.map_view;

/**
 * Output boundary for map update use case
 */
public interface UpdateMapOutputBoundary {
    /**
     * Presents the results of the Update Map use case.
     *
     * @param outputData the output data containing updated plane positions,
     *                   map bounds, and status information
     */
    void present(UpdateMapOutputData outputData);
}
