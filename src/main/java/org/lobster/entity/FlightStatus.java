package org.lobster.entity;


public enum FlightStatus {
    SCHEDULED("Scheduled", "🟢", "On Time"),
    DELAYED("Delayed", "🟡", "Delayed"),
    CANCELLED("Cancelled", "🔴", "Cancelled"),
    IN_AIR("In Air", "🔵", "In Air"),
    LANDED("Landed", "⚫", "Landed");

    private final String displayName;
    private final String colorCode;
    private final String shortDescription;

    FlightStatus(String displayName, String colorCode, String shortDescription) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.shortDescription = shortDescription;
    }

    public String getColorCode() { return colorCode; }
    public String getDisplayName() { return displayName; }
}