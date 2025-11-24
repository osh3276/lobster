package org.lobster.interface_adapter.browse_airport;

import org.lobster.entity.Flight;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class BrowseAirportViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<Flight> flights;
    private String message;

    public List<Flight> getFlights() {
        return flights;
    }

    public String getMessage(){
        return message;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
        firePropertyChanged();
    }

    public void setMessage(String message){
        this.message = message;
        firePropertyChanged();
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
