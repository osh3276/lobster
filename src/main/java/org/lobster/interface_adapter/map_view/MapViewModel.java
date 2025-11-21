package org.lobster.interface_adapter.map_view;

import org.lobster.entity.MapBounds;
import org.lobster.entity.MapPlane;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for the map view component
 */
public class MapViewModel {
    
    public static class MapState {
        private final List<MapPlane> planes;
        private final MapBounds mapBounds;
        private final boolean success;
        private final String message;

        public MapState(List<MapPlane> planes, MapBounds mapBounds, boolean success, String message) {
            this.planes = planes != null ? new ArrayList<>(planes) : new ArrayList<>();
            this.mapBounds = mapBounds;
            this.success = success;
            this.message = message;
        }

        public List<MapPlane> getPlanes() {
            return new ArrayList<>(planes);
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

    private MapState state;
    private final PropertyChangeSupport support;

    public MapViewModel() {
        this.state = new MapState(new ArrayList<>(), null, true, "Map not initialized");
        this.support = new PropertyChangeSupport(this);
    }

    public void setState(MapState newState) {
        MapState oldState = this.state;
        this.state = newState;
        support.firePropertyChange("state", oldState, newState);
    }

    public MapState getState() {
        return state;
    }

    public List<MapPlane> getPlanes() {
        return state.getPlanes();
    }

    public MapBounds getMapBounds() {
        return state.getMapBounds();
    }

    public String getMessage() {
        return state.getMessage();
    }

    public boolean isSuccess() {
        return state.isSuccess();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}