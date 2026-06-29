package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;
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
        if (lines.isEmpty()) {
            return new Money(BigDecimal.ZERO, "USD");
        }

        Money zero = new Money(BigDecimal.ZERO, lines.get(0).unitPrice().currency());
        Money subtotal = lines.stream()
                .map(line -> switch (line) {
                    case OrderLine(var product, var qty, Money(var amount, var currency)) ->
                            new Money(amount.multiply(BigDecimal.valueOf(qty)), currency);
                })
                .reduce(zero, Money::plus);

        if (discount == null) {
            return subtotal;
        }

        BigDecimal multiplier = BigDecimal.ONE.subtract(
                discount.percent().divide(BigDecimal.valueOf(100))
        );
        return new Money(subtotal.amount().multiply(multiplier), subtotal.currency());
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
