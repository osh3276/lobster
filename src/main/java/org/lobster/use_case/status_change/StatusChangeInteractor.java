package org.lobster.use_case.status_change;

import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FlightDataAccessInterface;

/**
 * Interactor for the Status Change use case.
 */
public class StatusChangeInteractor implements StatusChangeInputBoundary {

    private final FavoriteFlightsDataAccessInterface favoritesDAO;
    private final FlightDataAccessInterface flightAPI;
    private final StatusChangeOutputBoundary presenter;

    /**
     * Constructs a new StatusChangeInteractor.
     *
     * @param favoritesDAO the data access interface for stored favorite flights
     * @param flightAPI    the external flight data provider used to fetch updates
     * @param presenter    the output boundary responsible for presenting changes
     */
    public StatusChangeInteractor(FavoriteFlightsDataAccessInterface favoritesDAO,
                                  FlightDataAccessInterface flightAPI,
                                  StatusChangeOutputBoundary presenter) {
        this.favoritesDAO = favoritesDAO;
        this.flightAPI = flightAPI;
        this.presenter = presenter;
    }

    @Override
    public void execute(StatusChangeInputData inputData) {
        for (Flight f : favoritesDAO.findAll()) {
            Flight latest = flightAPI.findByFlightNumber(f.getFlightNumber());

            if (latest.getStatus() == null) {
                return;
            }

            if (!latest.getStatus().equals(f.getStatus())) {
                favoritesDAO.save(latest);
                presenter.present(new StatusChangeOutputData(latest));
            }
        }
    }
}
