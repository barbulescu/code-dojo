package com.barbulescu.codedojo.exercise0003;

import java.util.List;

public record CreateOrder(String orderId, String customerId, List<OrderLine> lines) implements OrderCommand {
    public CreateOrder {
        if (isBlank(orderId)) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        if (isBlank(customerId)) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (lines == null) {
            throw new IllegalArgumentException("lines must not be null");
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be empty");
        }
        for (OrderLine line : lines) {
            if (line == null) {
                throw new IllegalArgumentException("lines must not contain null elements");
            }
        }
        lines = List.copyOf(lines);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
