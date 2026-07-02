package com.barbulescu.codedojo.exercise0003;

public record AddItem(String productId, String productName, int quantity, Money unitPrice) implements OrderCommand {
    public AddItem {
        if (isBlank(productId)) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (isBlank(productName)) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice must not be null");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
