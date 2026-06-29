package com.barbulescu.codedojo.exercise0003;

public class Shipped implements FulfillmentState {

    private String trackingNumber;

    public Shipped(String trackingNumber) {
        setTrackingNumber(trackingNumber);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String trackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }
        this.trackingNumber = trackingNumber;
    }
}
