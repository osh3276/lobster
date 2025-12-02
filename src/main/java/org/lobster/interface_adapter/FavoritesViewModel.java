
package org.lobster.interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.lobster.entity.Flight;

/**
 * ViewModel for managing UI-facing state related to favorite flights.
 */
public class FavoritesViewModel {
    private State state = new State(false, "Ready", null, new ArrayList<>());
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Immutable structure representing the UI-ready state of the favorites list.
     */
    public static class State {
        public final boolean success;
        public final String message;
        public final Flight affectedFlight;
        public final List<Flight> allFavorites;

        /**
         * Constructs a new immutable state object.
         *
         * @param success        whether the use case operation succeeded
         * @param message        a message describing the result of the operation
         * @param affectedFlight the flight affected by the operation, or {@code null}
         * @param allFavorites   the complete list of favorite flights
         */
        public State(boolean success, String message, Flight affectedFlight, List<Flight> allFavorites) {
            this.success = success;
            this.message = message;
            this.affectedFlight = affectedFlight;
            this.allFavorites = allFavorites;
        }
    }

    /**
     * Returns the current ViewModel state.
     *
     * @return the current state object
     */
    public State getState() {
        return state;
    }

    /**
     * Updates the ViewModel with a new state and notifies observers.
     *
     * @param newState the new state to apply
     */
    public void setState(State newState) {
        System.out.println("DEBUG: ViewModel - Setting new state. Favorites count: " + newState.allFavorites.size());
        State oldState = this.state;
        this.state = newState;
        System.out.println("DEBUG: ViewModel - Firing property change event");
        support.firePropertyChange("state", oldState, this.state);
    }

    /**
     * Registers an observer that will be notified whenever the state changes.
     *
     * @param listener the property change listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("DEBUG: ViewModel - Adding property change listener: "
                + listener.getClass().getSimpleName());
        support.addPropertyChangeListener(listener);
    }
}
