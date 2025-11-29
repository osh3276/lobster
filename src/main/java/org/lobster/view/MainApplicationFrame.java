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
import org.lobster.view.BrowseAirportPanel;

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
                                BrowseAirportViewModel browseAirportViewModel) {

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

        favoritesViewModel.addPropertyChangeListener(this);
        browseAirportViewModel.addPropertyChangeListener(this);
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
        mainPanel.add(browseAirportPanel(), BorderLayout.SOUTH);

        // Create a split pane for the result area and map
        JSplitPane mainContentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainContentSplitPane.setDividerLocation(300);
        mainContentSplitPane.setResizeWeight(0.6);
        
        mainContentSplitPane.setTopComponent(createResultArea());
        
        // Create the map panel
        mapPanel = new MapPanel(mapViewController, mapViewModel);
        mainContentSplitPane.setBottomComponent(mapPanel);
        
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

    private JPanel browseAirportPanel() {
        return new BrowseAirportPanel(browseAirportController, browseAirportViewModel);
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
                    statusLabel.setText("✓ " + state.message);
                    statusLabel.setForeground(Color.GREEN);

                    if (state.affectedFlight != null) {
                        if (state.message.contains("added") || state.message.contains("saved")) {
                            // This was an ADD operation
                            resultArea.setText("Successfully added to favorites!\n\n" +
                                    "Flight: " + state.affectedFlight.getCallsign() + "\n" +
                                    "Airline: " + state.affectedFlight.getAirline() + "\n" +
                                    "Route: " + state.affectedFlight.getDeparture().getName() + " → " +
                                    state.affectedFlight.getArrival().getName() + "\n");
                        } else if (state.message.contains("removed")) {
                            // This was a REMOVE operation
                            resultArea.setText("Successfully removed from favorites!");
                        }
                    }
                } else {
                    statusLabel.setText("✗ " + state.message);
                    statusLabel.setForeground(Color.RED);
                }
            });
        }
        if (evt.getSource() == browseAirportViewModel && "state".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(() -> {
                var flights = browseAirportViewModel.getFlights();
                var message = browseAirportViewModel.getMessage();

                StringBuilder sb = new StringBuilder();

                if (flights != null && !flights.isEmpty()) {
                    sb.append("Airport results:\n\n");
                    for (var f : flights) {
                        sb.append(f).append("\n");
                    }
                }

                sb.append("\n").append(message);
                resultArea.setText(sb.toString());

                statusLabel.setText(message);
                statusLabel.setForeground((flights != null && !flights.isEmpty()) ? Color.GREEN : Color.RED);
            });
        }
    }

    private void performBrowseAirport(String airportCode, String type) {
        if (airportCode == null || airportCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an airport code");
            return;
        }

        resultArea.setText("Fetching " + type + " for " + airportCode + "...");

        browseAirportController.onBrowse(airportCode, type);

        var flights = browseAirportViewModel.getFlights();
        var message = browseAirportViewModel.getMessage();

        StringBuilder sb = new StringBuilder();

        if (flights != null && !flights.isEmpty()) {
            sb.append("Results for ").append(airportCode).append(" (").append(type).append("):\n\n");

            for (var f : flights) {
                sb.append("• ").append(f.getCallsign())
                        .append(" — ").append(f.getDeparture().getIata())
                        .append(" → ").append(f.getArrival().getIata()).append("\n");
            }

            // update map with all flight positions
            java.util.List<String> flightNumbers = flights.stream()
                    .map(f -> f.getCallsign())
                    .toList();

            mapPanel.updatePlanePositions(flightNumbers);

        } else {
            sb.append("No ").append(type).append(" for ").append(airportCode).append(".\n");
        }

        sb.append("\n").append(message);
        resultArea.setText(sb.toString());

        statusLabel.setText(message);
        statusLabel.setForeground((flights != null && !flights.isEmpty()) ? Color.GREEN : Color.RED);
    }
}