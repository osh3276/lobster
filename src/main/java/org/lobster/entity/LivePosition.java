package org.lobster.entity;

public class LivePosition {
    private final double latitude;
    private final double longitude;
    // in feet
    private final int altitude;
    // in knots
    private final double groundSpeed;
    // heading, in degrees
    private final double track;

    public LivePosition(double latitude, double longitude, int altitude,
                        double gspeed, double track) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.groundSpeed = gspeed;
        this.track = track;
    }

    public boolean isValid() {
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    // Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getGroundSpeed() {
        return groundSpeed;
    }

    public double getTrack() {
        return track;
    }
}
