package com.barbulescu.codedojo.exercise0003;

public record Accepted(Order order) implements OrderResult {
    public Accepted {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
    }
}
