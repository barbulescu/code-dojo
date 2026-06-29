package com.barbulescu.codedojo.exercise0003;

public record ShipOrder(String trackingNumber) implements OrderCommand {
    public ShipOrder {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }
    }
}
