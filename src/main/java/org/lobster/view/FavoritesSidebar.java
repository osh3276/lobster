package org.lobster.view;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoritesViewModel;
import org.lobster.interface_adapter.export_flights.ExportFlightsController;
import org.lobster.interface_adapter.get_favorites.GetFavoritesController;
import org.lobster.interface_adapter.remove_from_favorites.RemoveFromFavoritesController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class FavoritesSidebar extends JPanel implements PropertyChangeListener {
    private final GetFavoritesController getFavoritesController;
    private final RemoveFromFavoritesController removeFromFavoritesController;
    private final ExportFlightsController exportFlightsController;
    private final FavoritesViewModel favoritesViewModel;
    private final JList<Flight> favoritesList;
    private final DefaultListModel<Flight> listModel;
    private final JLabel countLabel;
    private final List<JCheckBox> checkBoxes;

    public FavoritesSidebar(GetFavoritesController getFavoritesController,
                            RemoveFromFavoritesController removeFromFavoritesController,
                            ExportFlightsController exportFlightsController,
                            FavoritesViewModel favoritesViewModel) {
        this.getFavoritesController = getFavoritesController;
        this.removeFromFavoritesController = removeFromFavoritesController;
        this.exportFlightsController = exportFlightsController;
        this.favoritesViewModel = favoritesViewModel;
        this.checkBoxes = new ArrayList<>();

        favoritesViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create titled border
        TitledBorder border = BorderFactory.createTitledBorder("Favorite Flights");
        border.setTitleJustification(TitledBorder.CENTER);
        setBorder(border);

        // Count label
        countLabel = new JLabel("No favorites");
        countLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(countLabel, BorderLayout.NORTH);

        // Favorites list with checkboxes
        listModel = new DefaultListModel<>();
        favoritesList = new JList<>(listModel);
        favoritesList.setCellRenderer(new FlightListRendererWithCheckbox());
        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Handle checkbox clicks
        favoritesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = favoritesList.locationToIndex(e.getPoint());
                if (index >= 0 && index < checkBoxes.size()) {
                    Rectangle cellBounds = favoritesList.getCellBounds(index, index);
                    if (cellBounds != null && cellBounds.contains(e.getPoint())) {
                        int checkboxX = cellBounds.x + 5;
                        int checkboxWidth = 20;
                        if (e.getX() >= checkboxX && e.getX() <= checkboxX + checkboxWidth) {
                            JCheckBox checkbox = checkBoxes.get(index);
                            checkbox.setSelected(!checkbox.isSelected());
                            favoritesList.repaint();
                        }
                    }
                }
            }
        });

        // Add right-click context menu for removal
        setupContextMenu();

        JScrollPane scrollPane = new JScrollPane(favoritesList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Control panel with buttons
        add(createControlPanel(), BorderLayout.SOUTH);

        // Load initial favorites
        refreshFavorites();
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshFavorites());

        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> removeSelectedFavorite());

        JButton exportSelectedButton = new JButton("Export Selected Flights");
        exportSelectedButton.addActionListener(e -> exportSelectedFlights());

        JButton exportAllButton = new JButton("Export All Favorites");
        exportAllButton.addActionListener(e -> exportAllFavorites());

        controlPanel.add(refreshButton);
        controlPanel.add(removeButton);
        controlPanel.add(exportSelectedButton);
        controlPanel.add(exportAllButton);

        return controlPanel;
    }

    private void setupContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove from Favorites");
        removeItem.addActionListener(e -> removeSelectedFavorite());
        contextMenu.add(removeItem);

        favoritesList.setComponentPopupMenu(contextMenu);

        // Work here to try and allow for deselection functionality
        favoritesList.addMouseListener(new MouseAdapter() {
            private int lastSelectedIndex = -1;

            @Override
            public void mouseReleased(MouseEvent e) {
                int index = favoritesList.locationToIndex(e.getPoint());

                if (index == -1) {
                    // Clicked on empty space - clear selection
                    favoritesList.clearSelection();
                    lastSelectedIndex = -1;
                } else {
                    // Check if clicking the same item that was already selected
                    if (index == lastSelectedIndex) {
                        // Clicked same item twice - deselect it
                        favoritesList.clearSelection();
                        lastSelectedIndex = -1;
                    } else {
                        // Clicked different item - select it and remember it
                        favoritesList.setSelectedIndex(index);
                        lastSelectedIndex = index;
                    }
                }
            }
        });
    }

    private void removeSelectedFavorite() {
        Flight selectedFlight = favoritesList.getSelectedValue();
        if (selectedFlight != null) {
            // Use flight number instead of callsign
            String flightNumber = selectedFlight.getFlightNumber();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove " + flightNumber + " from favorites?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                removeFromFavoritesController.execute(flightNumber);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to remove");
        }
    }

    private void refreshFavorites() {
        getFavoritesController.execute();
    }

    private void updateFavoritesList(List<Flight> favorites) {
        listModel.clear();
        checkBoxes.clear();
        for (Flight flight : favorites) {
            listModel.addElement(flight);
            checkBoxes.add(new JCheckBox());
        }
        updateCountLabel(favorites.size());
    }

    private void exportSelectedFlights() {
        List<Flight> selectedFlights = getSelectedFlights();
        if (selectedFlights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one flight to export",
                    "No Flights Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] formats = {"CSV"};
        String selectedFormat = (String) JOptionPane.showInputDialog(
                this,
                "Select export format:",
                "Export Format",
                JOptionPane.QUESTION_MESSAGE,
                null,
                formats,
                formats[0]
        );

        if (selectedFormat != null) {
            exportFlightsController.execute(selectedFlights, selectedFormat, false);
        }
    }

    private void exportAllFavorites() {
        List<Flight> allFavorites = favoritesViewModel.getState().allFavorites;
        if (allFavorites.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No favorites to export",
                    "No Favorites", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] formats = {"CSV"};
        String selectedFormat = (String) JOptionPane.showInputDialog(
                this,
                "Select export format:",
                "Export Format",
                JOptionPane.QUESTION_MESSAGE,
                null,
                formats,
                formats[0]
        );

        if (selectedFormat != null) {
            exportFlightsController.execute(allFavorites, selectedFormat, true);
        }
    }

    private List<Flight> getSelectedFlights() {
        List<Flight> selected = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size() && i < listModel.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selected.add(listModel.getElementAt(i));
            }
        }
        return selected;
    }

    private void updateCountLabel(int count) {
        if (count == 0) {
            countLabel.setText("No favorites");
        } else if (count == 1) {
            countLabel.setText("1 favorite flight");
        } else {
            countLabel.setText(count + " favorite flights");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(() -> {
                var state = favoritesViewModel.getState();
                updateFavoritesList(state.allFavorites);

                // Show removal confirmation in status if needed
                if (state.success && state.affectedFlight == null && state.message.contains("removed")) {
                    // This was a removal operation
                    JOptionPane.showMessageDialog(this, state.message, "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
    }

    // Custom renderer for flight items in the list with checkbox
    private class FlightListRendererWithCheckbox extends JPanel implements ListCellRenderer<Flight> {
        private final JCheckBox checkBox;
        private final JLabel flightNumberLabel;
        private final JLabel routeLabel;
        private final JLabel statusLabel;

        public FlightListRendererWithCheckbox() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 5, 5, 5));

            checkBox = new JCheckBox();
            checkBox.setEnabled(false);

            flightNumberLabel = new JLabel();
            flightNumberLabel.setFont(flightNumberLabel.getFont().deriveFont(Font.BOLD));

            routeLabel = new JLabel();
            routeLabel.setFont(routeLabel.getFont().deriveFont(11f));

            statusLabel = new JLabel();
            statusLabel.setFont(statusLabel.getFont().deriveFont(11f));

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.add(flightNumberLabel, BorderLayout.NORTH);
            infoPanel.add(routeLabel, BorderLayout.CENTER);

            add(checkBox, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            add(statusLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Flight> list, Flight flight,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            if (index >= 0 && index < checkBoxes.size()) {
                checkBox.setSelected(checkBoxes.get(index).isSelected());
            } else {
                checkBox.setSelected(false);
            }

            flightNumberLabel.setText(flight.getCallsign() + " - " + flight.getAirline());
            routeLabel.setText(flight.getDeparture().getIata() + " → " + flight.getArrival().getIata());
            if (flight.getStatus() != null) {
                statusLabel.setText(flight.getStatus().getColorCode() + " " + flight.getStatus().getDisplayName());
            } else {
                statusLabel.setText("N/A");
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);

            return this;
        }
    }

    public JList<Flight> getFavoritesList() {
        return favoritesList;
    }
}