package org.lobster.view;

import org.lobster.entity.Flight;
import org.lobster.interface_adapter.FavoritesViewModel;
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
import java.util.List;

public class FavoritesSidebar extends JPanel implements PropertyChangeListener {
    private final GetFavoritesController getFavoritesController;
    private final RemoveFromFavoritesController removeFromFavoritesController;
    private final FavoritesViewModel favoritesViewModel;
    private final JList<Flight> favoritesList;
    private final DefaultListModel<Flight> listModel;
    private final JLabel countLabel;

    public FavoritesSidebar(GetFavoritesController getFavoritesController,
                            RemoveFromFavoritesController removeFromFavoritesController,
                            FavoritesViewModel favoritesViewModel) {
        this.getFavoritesController = getFavoritesController;
        this.removeFromFavoritesController = removeFromFavoritesController;
        this.favoritesViewModel = favoritesViewModel;

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

        // Favorites list
        listModel = new DefaultListModel<>();
        favoritesList = new JList<>(listModel);
        favoritesList.setCellRenderer(new FlightListRenderer());
        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshFavorites());

        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> removeSelectedFavorite());

        controlPanel.add(refreshButton);
        controlPanel.add(removeButton);

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
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove " + selectedFlight.getCallsign() + " from favorites?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                removeFromFavoritesController.execute(selectedFlight.getCallsign());
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
        for (Flight flight : favorites) {
            listModel.addElement(flight);
        }
        updateCountLabel(favorites.size());
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
                if (state.success && state.lastAddedFlight == null && state.message.contains("removed")) {
                    // This was a removal operation
                    JOptionPane.showMessageDialog(this, state.message, "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
    }

    // Custom renderer for flight items in the list
    private static class FlightListRenderer extends JPanel implements ListCellRenderer<Flight> {
        private final JLabel flightNumberLabel;
        private final JLabel routeLabel;
        private final JLabel statusLabel;

        public FlightListRenderer() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 5, 5, 5));

            flightNumberLabel = new JLabel();
            flightNumberLabel.setFont(flightNumberLabel.getFont().deriveFont(Font.BOLD));

            routeLabel = new JLabel();
            routeLabel.setFont(routeLabel.getFont().deriveFont(11f));

            statusLabel = new JLabel();
            statusLabel.setFont(statusLabel.getFont().deriveFont(11f));

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.add(flightNumberLabel, BorderLayout.NORTH);
            infoPanel.add(routeLabel, BorderLayout.CENTER);

            add(infoPanel, BorderLayout.CENTER);
            add(statusLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Flight> list, Flight flight,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            flightNumberLabel.setText(flight.getCallsign() + " - " + flight.getAirline());
            routeLabel.setText(flight.getDeparture().getIata() + " → " + flight.getArrival().getIata());
            statusLabel.setText(flight.getStatus().getColorCode() + " " + flight.getStatus().getDisplayName());

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