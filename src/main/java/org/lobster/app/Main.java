package org.lobster.app;

import org.lobster.data_access.FlightRadarDataAccess;
import org.lobster.data_access.InMemoryFavoriteFlightsDAO;
import org.lobster.entity.Flight;
//Interface Adapters
import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.interface_adapter.FavoriteFlightsDataAccessInterface;
import org.lobster.interface_adapter.FlightDataAccessInterface;
import org.lobster.interface_adapter.add_to_favorites.AddToFavoritesController;
import org.lobster.interface_adapter.add_to_favorites.AddToFavoritesPresenter;
import org.lobster.interface_adapter.get_favorites.GetFavoritesController;
import org.lobster.interface_adapter.get_favorites.GetFavoritesPresenter;
import org.lobster.interface_adapter.remove_from_favorites.RemoveFromFavoritesController;
import org.lobster.interface_adapter.remove_from_favorites.RemoveFromFavoritesPresenter;

import org.lobster.interface_adapter.search_flight.SearchFlightController;
import org.lobster.interface_adapter.search_flight.SearchFlightPresenter;
import org.lobster.interface_adapter.search_flight.SearchFlightViewModel;
import org.lobster.use_case.add_to_favorites.AddToFavoritesInputBoundary;
import org.lobster.use_case.add_to_favorites.AddToFavoritesInteractor;
import org.lobster.use_case.add_to_favorites.AddToFavoritesOutputBoundary;
import org.lobster.use_case.get_favorites.GetFavoritesInputBoundary;
import org.lobster.use_case.get_favorites.GetFavoritesInteractor;
import org.lobster.use_case.get_favorites.GetFavoritesOutputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesInteractor;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesOutputBoundary;


import org.lobster.use_case.search_flight.SearchFlightInputBoundary;
import org.lobster.use_case.search_flight.SearchFlightInteractor;
import org.lobster.use_case.search_flight.SearchFlightOutputBoundary;
import org.lobster.view.MainApplicationFrame;

import javax.swing.*;

public class Main {
  
    private static boolean debugMode = false;

    // Configuration methods - ADD THESE METHODS TO YOUR MAIN CLASS
    public FlightDataAccessInterface flightDataAccess() {
        return new FlightRadarDataAccess();
    }

    public FavoriteFlightsDataAccessInterface favoriteFlightsDataAccess() {
        return new InMemoryFavoriteFlightsDAO();
    }

    public FavoritesViewModel favoritesViewModel() {
        return new FavoritesViewModel();
    }

    public SearchFlightViewModel searchFlightViewModel() { return new SearchFlightViewModel(); }
  
    public static void testAPI(String callsign) {
        try {
            FlightRadarDataAccess api = new FlightRadarDataAccess();
            System.out.println(api.findByCallSign(callsign));
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Rest of your existing main method
    public static void main(String[] args) {
        if (debugMode) { testAPI("AC123"); }
        // Create an instance of Main to access the configuration methods
        Main appConfig = new Main();

        // Set up dependencies using the configuration methods
        FavoriteFlightsDataAccessInterface favoritesDAO = appConfig.favoriteFlightsDataAccess();
        FlightDataAccessInterface flightDataAccess = appConfig.flightDataAccess();
        FavoritesViewModel favoritesViewModel = appConfig.favoritesViewModel();
        SearchFlightViewModel searchFlightViewModel = appConfig.searchFlightViewModel();

        // Add to favorites use case
        AddToFavoritesOutputBoundary addToFavoritesOutputBoundary = new AddToFavoritesPresenter(favoritesViewModel);
        AddToFavoritesInputBoundary addToFavoritesInteractor = new AddToFavoritesInteractor(
                favoritesDAO, flightDataAccess, addToFavoritesOutputBoundary
        );
        AddToFavoritesController addToFavoritesController = new AddToFavoritesController(addToFavoritesInteractor);

        // Get favorites use case
        GetFavoritesOutputBoundary getFavoritesOutputBoundary = new GetFavoritesPresenter(favoritesViewModel);
        GetFavoritesInputBoundary getFavoritesInteractor = new GetFavoritesInteractor(
                favoritesDAO, getFavoritesOutputBoundary
        );
        GetFavoritesController getFavoritesController = new GetFavoritesController(getFavoritesInteractor);

        // Remove from favorites use case
        RemoveFromFavoritesOutputBoundary removeFromFavoritesOutputBoundary = new RemoveFromFavoritesPresenter(favoritesViewModel);
        RemoveFromFavoritesInputBoundary removeFromFavoritesInteractor = new RemoveFromFavoritesInteractor(
                favoritesDAO, flightDataAccess, removeFromFavoritesOutputBoundary
        );
        RemoveFromFavoritesController removeFromFavoritesController = new RemoveFromFavoritesController(removeFromFavoritesInteractor);

        // Search flights use case
        SearchFlightOutputBoundary searchFlightOutputBoundary = new SearchFlightPresenter(searchFlightViewModel);
        SearchFlightInputBoundary searchFlightInteractor = new SearchFlightInteractor(flightDataAccess, searchFlightOutputBoundary);
        SearchFlightController searchFlightController = new SearchFlightController(searchFlightInteractor);

        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainApplicationFrame frame = new MainApplicationFrame(
                    addToFavoritesController,
                    getFavoritesController,
                    removeFromFavoritesController,
                    favoritesViewModel,
                    searchFlightController,
                    searchFlightViewModel
            );
            frame.setVisible(true);
        });
    }
}