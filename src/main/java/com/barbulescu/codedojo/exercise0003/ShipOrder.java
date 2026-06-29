package com.barbulescu.codedojo.exercise0003;

public class ShipOrder implements OrderCommand {

    private String trackingNumber;

    public ShipOrder(String trackingNumber) {
        setTrackingNumber(trackingNumber);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String trackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        if (isBlank(trackingNumber)) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }
        this.trackingNumber = trackingNumber;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
