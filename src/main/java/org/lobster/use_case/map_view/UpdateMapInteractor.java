package org.lobster.use_case.map_view;

import org.lobster.entity.*;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.util.Logger;
import org.lobster.exception.MapException;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for updating map with flight positions
 */
public class UpdateMapInteractor implements UpdateMapInputBoundary {
    
    private static final String CLASS_NAME = "UpdateMapInteractor";
    private final FlightDataAccessInterface flightDataAccess;
    private final UpdateMapOutputBoundary presenter;
    
    // Mercator projection bounds (85°S to 85°N, full longitude range)
    private static final double DEFAULT_MIN_LAT = -85.0;
    private static final double DEFAULT_MAX_LAT = 85.0;
    private static final double DEFAULT_MIN_LON = -180.0;
    private static final double DEFAULT_MAX_LON = 180.0;

    public UpdateMapInteractor(FlightDataAccessInterface flightDataAccess, 
                              UpdateMapOutputBoundary presenter) {
        this.flightDataAccess = flightDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(UpdateMapInputData inputData) {
        try {
            List<MapPlane> mapPlanes = new ArrayList<>();
            List<String> flightNumbers = inputData.getFlightNumbers();
            
            // Create map bounds
            MapBounds mapBounds = new MapBounds(
                DEFAULT_MIN_LAT, DEFAULT_MAX_LAT,
                DEFAULT_MIN_LON, DEFAULT_MAX_LON,
                inputData.getMapWidth(), inputData.getMapHeight()
            );
            
            // If no specific flights requested, try to get all available flights
            if (flightNumbers == null || flightNumbers.isEmpty()) {
                // For now, we'll return empty since we don't have a "get all flights" method implemented
                UpdateMapOutputData outputData = new UpdateMapOutputData(
                    mapPlanes, mapBounds, true, 
                    "Map initialized - search for flights to see them on the map"
                );
                presenter.present(outputData);
                return;
            }
            
            // Fetch flight data for each requested flight
            for (String flightNumber : flightNumbers) {
                processFlightForMap(flightNumber, mapBounds, mapPlanes);
            }
            
            String message = mapPlanes.isEmpty() 
                ? "No flights found in map area" 
                : String.format("Showing %d flight(s) on map", mapPlanes.size());
            
            UpdateMapOutputData outputData = new UpdateMapOutputData(
                mapPlanes, mapBounds, true, message
            );
            
            presenter.present(outputData);
            
        } catch (Exception e) {
            Logger.getInstance().error(CLASS_NAME, "Failed to update map", e);
            UpdateMapOutputData errorData = new UpdateMapOutputData(
                new ArrayList<>(), null, false, 
                "Failed to update map: " + e.getMessage()
            );
            presenter.present(errorData);
            throw e;
        }
    }

    /**
     * Process a single flight for map display
     * 
     * @param flightNumber the flight number to process
     * @param mapBounds the map bounds for coordinate conversion
     * @param mapPlanes the list to add valid map planes to
     */
    private void processFlightForMap(String flightNumber, MapBounds mapBounds, List<MapPlane> mapPlanes) {
        try {
            Flight flight = flightDataAccess.findByFlightNumber(flightNumber);
            
            if (flight != null && flight.getLivePosition() != null) {
                LivePosition position = flight.getLivePosition();
                
                // Only add flights that are within our map bounds
                if (mapBounds.contains(position.getLatitude(), position.getLongitude())) {
                    MapCoordinate screenPos = mapBounds.worldToScreen(
                        position.getLatitude(), 
                        position.getLongitude()
                    );
                    
                    MapPlane mapPlane = new MapPlane(
                        flight.getFlightNumber(),
                        position,
                        screenPos
                    );
                    
                    mapPlanes.add(mapPlane);
                }
            }
        } catch (Exception e) {
            Logger.getInstance().warn(CLASS_NAME, "Failed to get position for flight " + flightNumber, e);
        }
    }
}