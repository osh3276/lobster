package org.lobster.entity;

/**
 * Represents a plane on the map with its position and visual properties
 */
public class MapPlane {
    private final String flightNumber;
    private final LivePosition position;
    private final MapCoordinate screenPosition;
    private final double heading; // in degrees

    public MapPlane(String flightNumber, LivePosition position, MapCoordinate screenPosition) {
        this.flightNumber = flightNumber;
        this.position = position;
        this.screenPosition = screenPosition;
        this.heading = position.getTrack();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public LivePosition getPosition() {
        return position;
    }

    public MapCoordinate getScreenPosition() {
        return screenPosition;
    }

    public double getHeading() {
        return heading;
    }

    public int getAltitude() {
        return position.getAltitude();
    }

    public double getGroundSpeed() {
        return position.getGroundSpeed();
    }

    @Override
    public String toString() {
        return String.format("MapPlane(flight=%s, pos=%s, heading=%.1f°)", 
                           flightNumber, screenPosition, heading);
    }
}