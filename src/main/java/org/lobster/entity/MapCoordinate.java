package org.lobster.entity;

/**
 * Represents a screen coordinate on the map component
 */
public class MapCoordinate {
    private final int x;
    private final int y;

    public MapCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("MapCoordinate(x=%d, y=%d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MapCoordinate that = (MapCoordinate) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}