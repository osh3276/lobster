package org.lobster.interface_adapter;

import org.lobster.entity.Flight;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel {
    private State state = new State(false, "Ready", null, new ArrayList<>());
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static class State {
        public final boolean success;
        public final String message;
        public final Flight affectedFlight;
        public final List<Flight> allFavorites;

        public State(boolean success, String message, Flight affectedFlight, List<Flight> allFavorites) {
            this.success = success;
            this.message = message;
            this.affectedFlight = affectedFlight;
            this.allFavorites = allFavorites;
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        System.out.println("DEBUG: ViewModel - Setting new state. Favorites count: " + newState.allFavorites.size());
        State oldState = this.state;
        this.state = newState;
        System.out.println("DEBUG: ViewModel - Firing property change event");
        support.firePropertyChange("state", oldState, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("DEBUG: ViewModel - Adding property change listener: " + listener.getClass().getSimpleName());
        support.addPropertyChangeListener(listener);
    }
}