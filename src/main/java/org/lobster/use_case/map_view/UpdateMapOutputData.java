package org.lobster.use_case.map_view;

import org.lobster.entity.MapPlane;
import org.lobster.entity.MapBounds;

import java.util.List;

/**
 * Output data containing updated plane positions and map bounds
 */
public class UpdateMapOutputData {
    private final List<MapPlane> planes;
    private final MapBounds mapBounds;
    private final boolean success;
    private final String message;
    /**
     * Constructs a new UpdateMapOutputData object.
     *
     * @param planes    the list of plane data objects to render on the map
     * @param mapBounds the bounding coordinates defining the visible map area
     * @param success   whether the map update operation succeeded
     * @param message   a message describing the outcome of the operation
     */
    public UpdateMapOutputData(List<MapPlane> planes,
                               MapBounds mapBounds,
                               boolean success,
                               String message) {
        this.planes = planes;
        this.mapBounds = mapBounds;
        this.success = success;
        this.message = message;
    }
    /**
     * Returns the planes to display on the map.
     *
     * @return a list of MapPlane objects
     */
    public List<MapPlane> getPlanes() {
        return planes;
    }
    /**
     * Returns the map bounds defining the visible map area.
     *
     * @return a MapBounds object
     */
    public MapBounds getMapBounds() {
        return mapBounds;
    }
    /**
     * Returns whether the map update operation was successful.
     *
     * @return true if the operation succeeded; @code false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * Returns the message describing the result of the map update operation.
     *
     * @return a descriptive result message
     */
    public String getMessage() {
        return message;
    }
}
