package org.lobster.entity;

/**
 * Represents the bounds and projection parameters for a map view
 */
public class MapBounds {
    private final double minLatitude;
    private final double maxLatitude;
    private final double minLongitude;
    private final double maxLongitude;
    private final int mapWidth;
    private final int mapHeight;

    public MapBounds(double minLatitude, double maxLatitude, 
                     double minLongitude, double maxLongitude,
                     int mapWidth, int mapHeight) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    /**
     * Convert world coordinates (latitude/longitude) to screen coordinates using Mercator projection
     */
    public MapCoordinate worldToScreen(double latitude, double longitude) {
        // Mercator projection
        double x = (longitude + 180.0) / 360.0 * mapWidth;
        
        // Convert latitude to Mercator Y coordinate
        double latRad = Math.toRadians(latitude);
        double mercatorY = Math.log(Math.tan(Math.PI/4 + latRad/2));
        
        // Convert Mercator bounds to screen coordinates
        double minLatRad = Math.toRadians(minLatitude);
        double maxLatRad = Math.toRadians(maxLatitude);
        double minMercatorY = Math.log(Math.tan(Math.PI/4 + minLatRad/2));
        double maxMercatorY = Math.log(Math.tan(Math.PI/4 + maxLatRad/2));
        
        double y = mapHeight - ((mercatorY - minMercatorY) / (maxMercatorY - minMercatorY) * mapHeight);
        
        return new MapCoordinate((int)x, (int)y);
    }

    /**
     * Check if the given coordinates are within the map bounds
     */
    public boolean contains(double latitude, double longitude) {
        return latitude >= minLatitude && latitude <= maxLatitude &&
               longitude >= minLongitude && longitude <= maxLongitude;
    }

    // Getters
    public double getMinLatitude() { return minLatitude; }
    public double getMaxLatitude() { return maxLatitude; }
    public double getMinLongitude() { return minLongitude; }
    public double getMaxLongitude() { return maxLongitude; }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }

    @Override
    public String toString() {
        return String.format("MapBounds(lat: %.2f to %.2f, lon: %.2f to %.2f, size: %dx%d)", 
                           minLatitude, maxLatitude, minLongitude, maxLongitude, mapWidth, mapHeight);
    }
}