package com.barbulescu.codedojo.exercise0003;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateOrder implements OrderCommand {

    private String orderId;
    private String customerId;
    private List<OrderLine> lines;

    public CreateOrder(String orderId, String customerId, List<OrderLine> lines) {
        setOrderId(orderId);
        setCustomerId(customerId);
        setLines(lines);
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
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be empty");
        }
        for (OrderLine line : lines) {
            if (line == null) {
                throw new IllegalArgumentException("lines must not contain null elements");
            }
        }
        this.lines = new ArrayList<>(lines);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
