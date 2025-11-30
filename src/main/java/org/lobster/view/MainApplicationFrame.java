package org.lobster.view;

import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.interface_adapter.add_to_favorites.AddToFavoritesController;
import org.lobster.interface_adapter.get_favorites.GetFavoritesController;
import org.lobster.interface_adapter.remove_from_favorites.RemoveFromFavoritesController;
import org.lobster.interface_adapter.export_flights.ExportFlightsController;
import org.lobster.interface_adapter.search_flight.SearchFlightController;
import org.lobster.interface_adapter.search_flight.SearchFlightViewModel;
import org.lobster.interface_adapter.map_view.MapViewController;
import org.lobster.interface_adapter.map_view.MapViewModel;
import org.lobster.interface_adapter.browse_airport.BrowseAirportController;
import org.lobster.interface_adapter.browse_airport.BrowseAirportViewModel;
import org.lobster.interface_adapter.status_change.StatusChangeController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class MainApplicationFrame extends JFrame implements PropertyChangeListener {
    private final transient AddToFavoritesController addToFavoritesController;
    private final transient GetFavoritesController getFavoritesController;
    private final transient RemoveFromFavoritesController removeFromFavoritesController;
    private final transient ExportFlightsController exportFlightsController;
    private final transient FavoritesViewModel favoritesViewModel;
    private final transient SearchFlightController searchFlightController;
    private final transient SearchFlightViewModel searchFlightViewModel;
    private final transient MapViewController mapViewController;
    private final transient MapViewModel mapViewModel;
    private final BrowseAirportController browseAirportController;
    private final BrowseAirportViewModel browseAirportViewModel;
    private final transient StatusChangeController statusChangeController;

    private JTextField searchField;
    private JButton removeFavoriteButton; // New remove button
    private JTextArea resultArea;
    private JLabel statusLabel;
    private MapPanel mapPanel;

    public MainApplicationFrame(AddToFavoritesController addToFavoritesController,
                                GetFavoritesController getFavoritesController,
                                RemoveFromFavoritesController removeFromFavoritesController,
                                ExportFlightsController exportFlightsController,
                                FavoritesViewModel favoritesViewModel,
                                SearchFlightController searchFlightController,
                                SearchFlightViewModel searchFlightViewModel,
                                MapViewController mapViewController,
                                MapViewModel mapViewModel,
                                BrowseAirportController browseAirportController,
                                BrowseAirportViewModel browseAirportViewModel,
                                StatusChangeController statusChangeController) {

        this.addToFavoritesController = addToFavoritesController;
        this.getFavoritesController = getFavoritesController;
        this.removeFromFavoritesController = removeFromFavoritesController;
        this.exportFlightsController = exportFlightsController;
        this.favoritesViewModel = favoritesViewModel;

        this.searchFlightController = searchFlightController;
        this.searchFlightViewModel = searchFlightViewModel;
        
        this.mapViewController = mapViewController;
        this.mapViewModel = mapViewModel;

        this.browseAirportController = browseAirportController;
        this.browseAirportViewModel = browseAirportViewModel;

        this.statusChangeController = statusChangeController;

        favoritesViewModel.addPropertyChangeListener(this);
        browseAirportViewModel.addPropertyChangeListener(this);
        statusChangeController.poll();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Plain Plane Tracker - Lobsters Team");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        createMainPanel();
        createStatusBar();

        setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        BrowseAirportPanel browseAirportPanel = new BrowseAirportPanel(
                browseAirportController,
                browseAirportViewModel
        );
        FavoritesSidebar favoritesSidebar;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.7);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);

        // Create a split pane for the result area and map
        JSplitPane mainContentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainContentSplitPane.setDividerLocation(300);
        mainContentSplitPane.setResizeWeight(0.6);
        
        mainContentSplitPane.setTopComponent(createResultArea());

        // Create second split pane for airport panel and map
        JSplitPane airportSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        airportSplitPane.setDividerLocation(150);
        airportSplitPane.setResizeWeight(0.3);

        airportSplitPane.setTopComponent(browseAirportPanel);

        // Create the map panel
        mapPanel = new MapPanel(mapViewController, mapViewModel);
        airportSplitPane.setBottomComponent(mapPanel);

        mainContentSplitPane.setBottomComponent(airportSplitPane);
        
        mainPanel.add(mainContentSplitPane, BorderLayout.CENTER);

        favoritesSidebar = new FavoritesSidebar(
                getFavoritesController,
                removeFromFavoritesController,
                exportFlightsController,
                favoritesViewModel
        );

        splitPane.setLeftComponent(mainPanel);
        splitPane.setRightComponent(favoritesSidebar);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JButton addFavoriteButton;
        JButton searchButton;
        JPanel searchPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(15);
        searchButton = new JButton("Search Flight");
        addFavoriteButton = new JButton("Add to Favorites");
        removeFavoriteButton = new JButton("Remove from Favorites");

        searchButton.addActionListener(e -> performSearch());
        addFavoriteButton.addActionListener(e -> addCurrentToFavorites());


        searchPanel.add(new JLabel("Flight Number:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addFavoriteButton);

        return searchPanel;
    }

    private JComponent createResultArea() {
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setText("Enter a flight number and click Search to get started.");

        return new JScrollPane(resultArea);
    }

    private void createStatusBar() {
        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void performSearch() {
        String flightNumber = searchField.getText().trim();
        if (flightNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a flight number");
            return;
        }
        resultArea.setText("Searching for flight " + flightNumber + "...");

        searchFlightController.onSearch(flightNumber);

        var flight = searchFlightViewModel.getFlight();
        var message = searchFlightViewModel.getMessage();
        StringBuilder sb = new StringBuilder();
        if (flight != null) {
            sb.append(flight).append("\n\n");
            
            // Update the map with the searched flight
            java.util.List<String> flightNumbers = List.of(flightNumber);
            mapPanel.updatePlanePositions(flightNumbers);
            sb.append(message);
        } else {
            sb.append("No flight found for ").append(flightNumber).append("\n\n");
        }

        resultArea.setText(sb.toString());
        statusLabel.setText(message);
        statusLabel.setForeground(flight != null ? Color.GREEN: Color.RED);
    }


    private void addCurrentToFavorites() {
        String flightNumber = searchField.getText().trim();
        if (!flightNumber.isEmpty()) {
            addToFavoritesController.execute(flightNumber);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a flight number first");
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(() -> {
                var state = favoritesViewModel.getState();
                if (state.success) {
                    if (state.affectedFlight != null) {
                        if (state.message.contains("added") || state.message.contains("saved")) {
                            // Success popup for ADD operation
                            String message = "Successfully added to favorites!\n\n" +
                                    "Flight: " + state.affectedFlight.getCallsign() + "\n" +
                                    "Airline: " + state.affectedFlight.getAirline() + "\n" +
                                    "Route: " + state.affectedFlight.getDeparture().getName() + " → " +
                                    state.affectedFlight.getArrival().getName();

                            JOptionPane.showMessageDialog(
                                    null, // or pass your main frame reference
                                    message,
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else if (state.message.contains("removed")) {
                            // Success popup for REMOVE operation
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Successfully removed from favorites!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                } else {
                    // Error popup
                    JOptionPane.showMessageDialog(
                            null,
                            state.message,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });
        }
    }
}