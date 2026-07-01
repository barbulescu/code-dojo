package com.barbulescu.codedojo.exercise0003;

public record CancelOrder(String reason) implements OrderCommand {

    public CancelOrder {
        if (isBlank(reason)) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
