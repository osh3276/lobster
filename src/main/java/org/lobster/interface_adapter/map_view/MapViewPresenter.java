package org.lobster.interface_adapter.map_view;

import org.lobster.use_case.map_view.UpdateMapOutputBoundary;
import org.lobster.use_case.map_view.UpdateMapOutputData;

/**
 * Presenter for map view operations.
 */
public class MapViewPresenter implements UpdateMapOutputBoundary {
    
    private final MapViewModel mapViewModel;

    public MapViewPresenter(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }

    @Override
    public void present(UpdateMapOutputData outputData) {
        MapViewModel.MapState newState = new MapViewModel.MapState(
            outputData.getPlanes(),
            outputData.getMapBounds(),
            outputData.isSuccess(),
            outputData.getMessage()
        );
        
        mapViewModel.setState(newState);
    }
}
