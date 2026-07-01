package com.barbulescu.codedojo.exercise0003;

public record OrderLine(Product product, int quantity, Money unitPrice) {

    public OrderLine {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice must not be null");
        }
    }

    public Money lineTotal() {
        return switch (this) {
            case OrderLine(Product ignored, int lineQuantity, Money price) -> price.multiply(lineQuantity);
        };
    }
}
