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

    public UpdateMapOutputData(List<MapPlane> planes, MapBounds mapBounds, boolean success, String message) {
        this.planes = planes;
        this.mapBounds = mapBounds;
        this.success = success;
        this.message = message;
    }

    public List<MapPlane> getPlanes() {
        return planes;
    }

    public MapBounds getMapBounds() {
        return mapBounds;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}