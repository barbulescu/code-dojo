package com.barbulescu.codedojo.exercise0003;

public record CancelOrder(String reason) implements OrderCommand {
    public CancelOrder {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }
}
