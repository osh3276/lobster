package org.lobster.use_case.export_flights;

import org.lobster.entity.Flight;
import org.lobster.entity.Airline;
import org.lobster.entity.Airport;
import org.lobster.entity.LivePosition;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FlightExporter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void exportFlightsToCSV(List<Flight> flights, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writeCSVHeader(writer);
            for (Flight flight : flights) {
                writeFlightToCSV(writer, flight);
            }
        }
    }

    private static void writeCSVHeader(FileWriter writer) throws IOException {
        writer.append("Flight Number,Callsign,Airline IATA,Airline ICAO,Airline Name,")
              .append("Departure IATA,Departure ICAO,Departure Name,")
              .append("Arrival IATA,Arrival ICAO,Arrival Name,")
              .append("ETA,Status,")
              .append("Latitude,Longitude,Altitude (ft),Ground Speed (kt),Heading (deg),")
              .append("Hex\n");
    }

    private static void writeFlightToCSV(FileWriter writer, Flight flight) throws IOException {
        writer.append(escapeCSV(flight.getFlightNumber())).append(",")
              .append(escapeCSV(flight.getCallsign())).append(",");

        Airline airline = flight.getAirline();
        if (airline != null) {
            writer.append(escapeCSV(airline.getIata())).append(",")
                  .append(escapeCSV(airline.getIcao())).append(",")
                  .append(escapeCSV(airline.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Airport departure = flight.getDeparture();
        if (departure != null) {
            writer.append(escapeCSV(departure.getIata())).append(",")
                  .append(escapeCSV(departure.getIcao())).append(",")
                  .append(escapeCSV(departure.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Airport arrival = flight.getArrival();
        if (arrival != null) {
            writer.append(escapeCSV(arrival.getIata())).append(",")
                  .append(escapeCSV(arrival.getIcao())).append(",")
                  .append(escapeCSV(arrival.getName())).append(",");
        } else {
            writer.append(",,,");
        }

        Date eta = flight.getEta();
        if (eta != null) {
            writer.append(escapeCSV(DATE_FORMAT.format(eta))).append(",");
        } else {
            writer.append(",");
        }

        if (flight.getStatus() != null) {
            writer.append(escapeCSV(flight.getStatus().getDisplayName())).append(",");
        } else {
            writer.append(",");
        }

        LivePosition livePosition = flight.getLivePosition();
        if (livePosition != null && livePosition.isValid()) {
            writer.append(String.valueOf(livePosition.getLatitude())).append(",")
                  .append(String.valueOf(livePosition.getLongitude())).append(",")
                  .append(String.valueOf(livePosition.getAltitude())).append(",")
                  .append(String.valueOf(livePosition.getGroundSpeed())).append(",")
                  .append(String.valueOf(livePosition.getTrack())).append(",");
        } else {
            writer.append(",,,,,");
        }

        writer.append(escapeCSV(flight.getHex())).append("\n");
    }

    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

