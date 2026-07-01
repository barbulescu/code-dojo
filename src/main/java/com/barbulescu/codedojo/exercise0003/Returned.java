package com.barbulescu.codedojo.exercise0003;

public record Returned(String reason) implements FulfillmentState {

    public Returned {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }
}
