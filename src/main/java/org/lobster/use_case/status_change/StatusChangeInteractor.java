package org.lobster.use_case.status_change;

import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.data_access.FlightRadarDataAccess;
import org.lobster.entity.Flight;

public class StatusChangeInteractor implements StatusChangeInputBoundary{

    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final FlightRadarDataAccess flightAPI;
    private final StatusChangeOutputBoundary presenter;

    public StatusChangeInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                  FlightRadarDataAccess flightAPI,
                                  StatusChangeOutputBoundary presenter) {
        this.favoritesDAO = favoritesDAO;
        this.flightAPI = flightAPI;
        this.presenter = presenter;
    }

    @Override
    public void execute(StatusChangeInputData inputData) {
        for (Flight f : favoritesDAO.findAll()) {
            Flight latest = flightAPI.findByFlightNumber(f.getFlightNumber());

            if (!latest.getStatus().equals(f.getStatus())) {
                favoritesDAO.save(latest);
                presenter.present(new StatusChangeOutputData(latest));
            }
        }
    }
}
