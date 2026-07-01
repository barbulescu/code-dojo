package com.barbulescu.codedojo.exercise0003;

public record ReturnOrder(String reason) implements OrderCommand {

    public ReturnOrder {
        if (isBlank(reason)) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
