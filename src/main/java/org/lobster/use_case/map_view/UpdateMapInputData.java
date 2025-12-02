package org.lobster.use_case.map_view;

import java.util.List;

/**
 * Input data for updating the map with flight positions.
 */
public class UpdateMapInputData {
    private final List<String> flightNumbers;
    private final int mapWidth;
    private final int mapHeight;
    /**
     * Constructs a new UpdateMapInputData object.
     *
     * @param flightNumbers the list of flight numbers to display on the map;
     *                      may be null or empty to initialize a blank map
     * @param mapWidth      the width of the map in pixels
     * @param mapHeight     the height of the map in pixels
     */
    public UpdateMapInputData(List<String> flightNumbers,
                              int mapWidth,
                              int mapHeight) {
        this.flightNumbers = flightNumbers;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }
    /**
     * Returns the list of flight numbers requested for display.
     *
     * @return a list of flight numbers, or null if none were provided
     */
    public List<String> getFlightNumbers() {
        return flightNumbers;
    }
    /**
     * Returns the map width.
     *
     * @return the width of the map in pixels
     */
    public int getMapWidth() {
        return mapWidth;
    }
    /**
     * Returns the map height.
     *
     * @return the height of the map in pixels
     */
    public int getMapHeight() {
        return mapHeight;
    }
}
