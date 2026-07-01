package com.barbulescu.codedojo.exercise0003;

public record Cancelled(String reason) implements FulfillmentState {
    public Cancelled {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }
}
