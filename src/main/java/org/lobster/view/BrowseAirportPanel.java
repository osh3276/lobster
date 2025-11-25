package org.lobster.view;

import org.lobster.interface_adapter.browse_airport.BrowseAirportController;
import org.lobster.interface_adapter.browse_airport.BrowseAirportViewModel;
import org.lobster.entity.Airport;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BrowseAirportPanel extends JPanel implements PropertyChangeListener {

    private final BrowseAirportController controller;
    private final BrowseAirportViewModel viewModel;

    private JTextField airportField;
    private JButton browseButton;
    private JTextArea airportResultArea;

    public BrowseAirportPanel(BrowseAirportController controller,
                              BrowseAirportViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Browse Airport"));

        JPanel inputPanel = new JPanel(new FlowLayout());
        airportField = new JTextField(6);

        browseButton = new JButton("Browse");
        browseButton.addActionListener(e ->
                controller.onBrowse(
                        airportField.getText().trim()
                )
        );

        inputPanel.add(new JLabel("Airport Code:"));
        inputPanel.add(airportField);
        inputPanel.add(browseButton);

        add(inputPanel, BorderLayout.NORTH);

        // Results Area
        airportResultArea = new JTextArea();
        airportResultArea.setEditable(false);
        airportResultArea.setMargin(new Insets(10, 10, 10, 10));

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(airportResultArea), BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        var airport = viewModel.getAirport();
        var message = viewModel.getMessage();

        StringBuilder sb = new StringBuilder();

        if (message != null && !message.isEmpty()) {
            sb.append(message).append("\n\n");
        }

        if (airport != null) {
            sb.append("Name: ").append(airport.getName()).append("\n");
            sb.append("IATA: ").append(airport.getIata()).append("\n");
            sb.append("ICAO: ").append(airport.getIcao()).append("\n");
        }

        airportResultArea.setText(sb.toString());
    }
}
