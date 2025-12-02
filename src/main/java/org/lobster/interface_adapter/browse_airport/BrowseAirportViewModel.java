package org.lobster.interface_adapter.browse_airport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import org.lobster.entity.Flight;

/**
 * View Model for the Browse Aiport Use Case.
 */
public class BrowseAirportViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<Flight> flights;
    private String message;

    public List<Flight> getFlights() {

        return flights;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Updates the list of flights for the airport and notifies observers.
     * @param flights "the flights at the airport.
     */
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
        firePropertyChanged();
    }

    /**
     * Updates the message for the user and notifies observers.
     * @param message "a message describing a status or error to show to the user"
     */
    public void setMessage(String message) {
        this.message = message;
        firePropertyChanged();
    }

    /**
     * Notifies all registered listeners that the ViewModel's state has changed.
     */
    public void firePropertyChanged() {

        support.firePropertyChange("state", null, null);
    }

    /**
     * Registers an observer to listen for state changes.
     * @param listener "The listener to register"
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously registered observer.
     * @param listener "Observer"
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
