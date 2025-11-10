package org.lobster.entity;

public class LivePosition {
    private final double latitude;
    private final double longitude;
    private final int altitude; // in feet
    private final double speedHorizontal; // in knots
    private final double direction; // in degrees

    public LivePosition(double latitude, double longitude, int altitude,
                        double speedHorizontal, double direction) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speedHorizontal = speedHorizontal;
        this.direction = direction;
    }

    public boolean isValid() {
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    // Getters
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getAltitude() { return altitude; }
    public double getSpeedHorizontal() { return speedHorizontal; }
    public double getDirection() { return direction; }
}