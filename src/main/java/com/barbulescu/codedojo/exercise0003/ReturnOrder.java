package com.barbulescu.codedojo.exercise0003;

public record ReturnOrder(String reason) implements OrderCommand {
    public ReturnOrder {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }
}
