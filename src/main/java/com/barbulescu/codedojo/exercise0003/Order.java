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
        lines = List.copyOf(lines);
        if (state == null) {
            throw new IllegalArgumentException("state must not be null");
        }
    }

    public Order withLines(List<OrderLine> lines) {
        return new Order(orderId, customerId, lines, discount, state);
    }

    public Order withDiscount(AppliedDiscount discount) {
        return new Order(orderId, customerId, lines, discount, state);
    }

    public Order withState(FulfillmentState state) {
        return new Order(orderId, customerId, lines, discount, state);
    }

    public Money total() {
        if (lines.isEmpty()) {
            return new Money(BigDecimal.ZERO, "USD");
        }

        Money total = lines.stream()
                .map(OrderLine::lineTotal)
                .reduce(new Money(BigDecimal.ZERO, lines.getFirst().unitPrice().currency()), Money::plus);

        if (discount == null) {
            return total;
        }

        BigDecimal multiplier = BigDecimal.ONE.subtract(
                discount.percent().divide(BigDecimal.valueOf(100))
        );
        return new Money(total.amount().multiply(multiplier), total.currency());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
