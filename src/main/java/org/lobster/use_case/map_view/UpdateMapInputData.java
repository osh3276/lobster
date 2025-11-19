package org.lobster.use_case.map_view;

import java.util.List;

/**
 * Input data for updating the map with flight positions
 */
public class UpdateMapInputData {
    private final List<String> flightNumbers;
    private final int mapWidth;
    private final int mapHeight;

    public UpdateMapInputData(List<String> flightNumbers, int mapWidth, int mapHeight) {
        this.flightNumbers = flightNumbers;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public List<String> getFlightNumbers() {
        return flightNumbers;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}