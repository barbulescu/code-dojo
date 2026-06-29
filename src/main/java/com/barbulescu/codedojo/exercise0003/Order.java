package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private String orderId;
    private String customerId;
    private List<OrderLine> lines;
    private AppliedDiscount discount;
    private FulfillmentState state;

    public Order(
            String orderId,
            String customerId,
            List<OrderLine> lines,
            AppliedDiscount discount,
            FulfillmentState state
    ) {
        setOrderId(orderId);
        setCustomerId(customerId);
        setLines(lines);
        setDiscount(discount);
        setState(state);
    }

    public String getOrderId() {
        return orderId;
    }

    public String orderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (isBlank(orderId)) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String customerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (isBlank(customerId)) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        this.customerId = customerId;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<OrderLine> lines() {
        return getLines();
    }

    public void setLines(List<OrderLine> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("lines must not be null");
        }
        for (OrderLine line : lines) {
            if (line == null) {
                throw new IllegalArgumentException("lines must not contain null elements");
            }
        }
        this.lines = new ArrayList<>(lines);
    }

    public AppliedDiscount getDiscount() {
        return discount;
    }

    public AppliedDiscount discount() {
        return discount;
    }

    public void setDiscount(AppliedDiscount discount) {
        this.discount = discount;
    }

    public FulfillmentState getState() {
        return state;
    }

    public FulfillmentState state() {
        return state;
    }

    public void setState(FulfillmentState state) {
        if (state == null) {
            throw new IllegalArgumentException("state must not be null");
        }
        this.state = state;
    }

    public Money total() {
        if (lines.isEmpty()) {
            return new Money(BigDecimal.ZERO, "USD");
        }

        Money total = new Money(BigDecimal.ZERO, lines.get(0).getUnitPrice().getCurrency());
        for (OrderLine line : lines) {
            total = total.plus(line.lineTotal());
        }

        if (discount == null) {
            return total;
        }

        BigDecimal multiplier = BigDecimal.ONE.subtract(
                discount.getPercent().divide(BigDecimal.valueOf(100))
        );
        return new Money(total.getAmount().multiply(multiplier), total.getCurrency());
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
