package org.lobster.view;

import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.interface_adapter.add_to_favorites.AddToFavoritesController;
import org.lobster.interface_adapter.get_favorites.GetFavoritesController;
import org.lobster.interface_adapter.remove_from_favorites.RemoveFromFavoritesController;
import org.lobster.interface_adapter.search_flight.SearchFlightController;
import org.lobster.interface_adapter.search_flight.SearchFlightViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainApplicationFrame extends JFrame implements PropertyChangeListener {
    private final AddToFavoritesController addToFavoritesController;
    private final GetFavoritesController getFavoritesController;
    private final RemoveFromFavoritesController removeFromFavoritesController;
    private final FavoritesViewModel favoritesViewModel;
    private final SearchFlightController searchFlightController;
    private final SearchFlightViewModel searchFlightViewModel;

    private JTextField searchField;
    private JButton searchButton;
    private JButton addFavoriteButton;
    private JButton removeFavoriteButton; // New remove button
    private JTextArea resultArea;
    private JLabel statusLabel;
    private FavoritesSidebar favoritesSidebar;

    public MainApplicationFrame(AddToFavoritesController addToFavoritesController,
                                GetFavoritesController getFavoritesController,
                                RemoveFromFavoritesController removeFromFavoritesController,
                                FavoritesViewModel favoritesViewModel,
                                SearchFlightController searchFlightController,
                                SearchFlightViewModel searchFlightViewModel) {
        this.addToFavoritesController = addToFavoritesController;
        this.getFavoritesController = getFavoritesController;
        this.removeFromFavoritesController = removeFromFavoritesController;
        this.favoritesViewModel = favoritesViewModel;

        this.searchFlightController = searchFlightController;
        this.searchFlightViewModel = searchFlightViewModel;

        favoritesViewModel.addPropertyChangeListener(this);
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
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.7);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        mainPanel.add(createResultArea(), BorderLayout.CENTER);

        favoritesSidebar = new FavoritesSidebar(
                getFavoritesController,
                removeFromFavoritesController,
                favoritesViewModel
        );

        splitPane.setLeftComponent(mainPanel);
        splitPane.setRightComponent(favoritesSidebar);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(15);
        searchButton = new JButton("Search Flight");
        addFavoriteButton = new JButton("Add to Favorites");
        removeFavoriteButton = new JButton("Remove from Favorites");

        searchButton.addActionListener(e -> performSearch());
        addFavoriteButton.addActionListener(e -> addCurrentToFavorites());
        removeFavoriteButton.addActionListener(e -> removeCurrentFromFavorites());

        searchPanel.add(new JLabel("Flight Number:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addFavoriteButton);
        searchPanel.add(removeFavoriteButton);

        return searchPanel;
    }

    private JComponent createResultArea() {
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setText("Enter a flight number and click Search to get started.\n\nTry: AC873, DL123, or BA456");

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
            sb.append(flight.toString()).append("\n\n");
        }
        sb.append(message);

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

    private void removeCurrentFromFavorites() {
        String flightNumber = searchField.getText().trim();
        if (!flightNumber.isEmpty()) {
            removeFromFavoritesController.execute(flightNumber);
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

                    if (state.lastAddedFlight != null) {
                        // This was an add operation
                        resultArea.setText("Successfully added to favorites!\n\n" +
                                "Flight: " + state.lastAddedFlight.getCallsign() + "\n" +
                                "Airline: " + state.lastAddedFlight.getAirline() + "\n" +
                                "Route: " + state.lastAddedFlight.getDeparture().getName() + " → " +
                                state.lastAddedFlight.getArrival().getName() + "\n"); // +
                                //"Status: " + state.lastAddedFlight.getStatus().getDisplayName() + " " +
                                // state.lastAddedFlight.getStatus().getColorCode());
                    }
                } else {
                    statusLabel.setText("✗ " + state.message);
                    statusLabel.setForeground(Color.RED);
                }
            });
        }
    }
}