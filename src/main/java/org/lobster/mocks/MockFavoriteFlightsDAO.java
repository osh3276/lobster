package org.lobster.mocks;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mock implementation of FavoriteFlightsDataAccessInterface for unit testing
 */
public class MockFavoriteFlightsDAO implements FavoriteFlightsDataAccessInterface {
    private List<Flight> favorites = new ArrayList<>();

    // Tracking for test verification
    public boolean saveCalled = false;
    public Flight lastSavedFlight = null;
    public boolean deleteCalled = false;
    public String lastDeletedFlightNumber = null;

    // Exception controls
    private boolean throwExceptionOnExists = false;
    private boolean throwExceptionOnSave = false;
    private boolean throwExceptionOnDelete = false;
    private boolean existsByFlightNumberResult = false;

    public void setExistsByFlightNumberResult(boolean result) {
        this.existsByFlightNumberResult = result;
    }

    public void setThrowExceptionOnExists(boolean throwExceptionOnExists) {
        this.throwExceptionOnExists = throwExceptionOnExists;
    }

    public void setThrowExceptionOnSave(boolean throwExceptionOnSave) {
        this.throwExceptionOnSave = throwExceptionOnSave;
    }

    public void setThrowExceptionOnDelete(boolean throwExceptionOnDelete) {
        this.throwExceptionOnDelete = throwExceptionOnDelete;
    }

    public void setFavoritesList(List<Flight> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public void reset() {
        this.favorites.clear();
        this.throwExceptionOnExists = false;
        this.throwExceptionOnSave = false;
        this.throwExceptionOnDelete = false;
        this.existsByFlightNumberResult = false;
        this.saveCalled = false;
        this.lastSavedFlight = null;
        this.deleteCalled = false;
        this.lastDeletedFlightNumber = null;
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        if (throwExceptionOnExists) {
            throw new RuntimeException("Mock database error in exists check");
        }

        // If we're manually controlling the result, return that
        if (existsByFlightNumberResult) {
            return true;
        }

        // Otherwise check the actual favorites list
        return favorites.stream()
                .anyMatch(flight -> flightNumber.equals(flight.getFlightNumber()));
    }

    @Override
    public void save(Flight flight) {
        if (throwExceptionOnSave) {
            throw new RuntimeException("Mock database error in save");
        }
        saveCalled = true;
        lastSavedFlight = flight;

        // Remove if already exists (update scenario)
        favorites.removeIf(f -> flight.getFlightNumber().equals(f.getFlightNumber()));
        favorites.add(flight);
    }

    @Override
    public List<Flight> findAll() {
        return Collections.unmodifiableList(favorites);
    }

    @Override
    public void deleteByFlightNumber(String flightNumber) {
        if (throwExceptionOnDelete) {
            throw new RuntimeException("Mock database error in delete");
        }
        deleteCalled = true;
        lastDeletedFlightNumber = flightNumber;
        favorites.removeIf(flight -> flightNumber.equals(flight.getFlightNumber()));
    }
}