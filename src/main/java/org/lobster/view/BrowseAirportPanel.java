package org.lobster.view;

import org.lobster.interface_adapter.browse_airport.BrowseAirportController;
import org.lobster.interface_adapter.browse_airport.BrowseAirportViewModel;
import org.lobster.entity.Flight;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BrowseAirportPanel extends JPanel implements PropertyChangeListener {

    private final BrowseAirportController controller;
    private final BrowseAirportViewModel viewModel;

    private JTextField airportField;
    private JComboBox<String> typeDropdown;
    private JButton browseButton;
    private JTextArea airportResultArea;

    public BrowseAirportPanel(BrowseAirportController controller,
                              BrowseAirportViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Browse Airport"));

        // TOP PANEL
        JPanel inputPanel = new JPanel(new FlowLayout());

        airportField = new JTextField(5);
        typeDropdown = new JComboBox<>(new String[]{"arrivals", "departures"});

        browseButton = new JButton("Browse");
        browseButton.addActionListener(e ->
                controller.onBrowse(
                        airportField.getText().trim(),
                        (String) typeDropdown.getSelectedItem()
                )
        );

        inputPanel.add(new JLabel("Airport Code:"));
        inputPanel.add(airportField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeDropdown);
        inputPanel.add(browseButton);

        add(inputPanel, BorderLayout.NORTH);

        // RESULTS AREA
        airportResultArea = new JTextArea();
        airportResultArea.setEditable(false);
        airportResultArea.setMargin(new Insets(10, 10, 10, 10));

        add(new JScrollPane(airportResultArea), BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        var flights = viewModel.getFlights();
        var message = viewModel.getMessage();

        StringBuilder sb = new StringBuilder();

        if (message != null) {
            sb.append(message).append("\n\n");
        }

        if (flights != null) {
            for (Flight f : flights) {
                sb.append(f.toString()).append("\n\n");
            }
        }

        airportResultArea.setText(sb.toString());
    }
}
