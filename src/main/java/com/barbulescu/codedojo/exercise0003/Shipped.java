package com.barbulescu.codedojo.exercise0003;

public record Shipped(String trackingNumber) implements FulfillmentState {
    public Shipped {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }
    }
}
