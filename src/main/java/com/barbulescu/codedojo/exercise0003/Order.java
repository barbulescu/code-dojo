package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record Order(
        String orderId,
        String customerId,
        List<OrderLine> lines,
        AppliedDiscount discount,
        FulfillmentState state
) {
    public Order {
        if (isBlank(orderId)) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        if (isBlank(customerId)) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (lines == null) {
            throw new IllegalArgumentException("lines must not be null");
        }
        for (OrderLine line : lines) {
            if (line == null) {
                throw new IllegalArgumentException("lines must not contain null elements");
            }
        }
        if (state == null) {
            throw new IllegalArgumentException("state must not be null");
        }
        lines = List.copyOf(lines);
    }

    public Money total() {
        Money zero = new Money(BigDecimal.ZERO, lines.getFirst().unitPrice().currency());
        Money subtotal = lines.stream()
                .map(OrderLine::lineTotal)
                .reduce(zero, Money::plus);

        if (discount == null) {
            return subtotal;
        }

        BigDecimal multiplier = BigDecimal.ONE.subtract(
                discount.percent().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN)
        );
        return new Money(subtotal.amount().multiply(multiplier), subtotal.currency());
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
