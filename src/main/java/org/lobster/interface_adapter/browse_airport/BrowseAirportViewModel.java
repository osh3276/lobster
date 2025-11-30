package org.lobster.interface_adapter.browse_airport;

import org.lobster.entity.Airport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BrowseAirportViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Airport airport;
    private String message;

    public Airport getAirport() { return airport; }
    public String getMessage() { return message; }

    public void setAirport(Airport airport) {
        this.airport = airport;
        fireChange();
    }

    public void setMessage(String message) {
        this.message = message;
        fireChange();
    }

    private void fireChange() {
        support.firePropertyChange("state", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }
}
