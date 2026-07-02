package com.barbulescu.codedojo.exercise0003;

public record ShipOrder(String trackingNumber) implements OrderCommand {
    public ShipOrder {
        if (isBlank(trackingNumber)) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
