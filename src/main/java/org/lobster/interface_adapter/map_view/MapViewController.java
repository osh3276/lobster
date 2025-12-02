
package org.lobster.interface_adapter.map_view;

import java.util.List;

import org.lobster.use_case.map_view.UpdateMapInputBoundary;
import org.lobster.use_case.map_view.UpdateMapInputData;

/**
 * Controller for map view operations.
 */
public class MapViewController {
    
    private final UpdateMapInputBoundary updateMapUseCase;

    public MapViewController(UpdateMapInputBoundary updateMapUseCase) {

        this.updateMapUseCase = updateMapUseCase;
    }

    /**
     * Update the map with positions of the given flights.
     * @param flightNumbers "a list of flight identifiers to display on the map"
     * @param mapHeight "the width of the map in UI pixels"
     * @param mapWidth "the height of the map in UI pixels"
     */
    public void updateMap(List<String> flightNumbers, int mapWidth, int mapHeight) {
        UpdateMapInputData inputData = new UpdateMapInputData(flightNumbers, mapWidth, mapHeight);
        updateMapUseCase.execute(inputData);
    }

    /**
     * Initialize empty map.
     */
    public void initializeMap(int mapWidth, int mapHeight) {
        updateMap(null, mapWidth, mapHeight);
    }
}
