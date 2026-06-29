package com.barbulescu.codedojo.exercise0003;

public class Accepted implements OrderResult {

    private Order order;

    public Accepted(Order order) {
        setOrder(order);
    }

    public Order getOrder() {
        return order;
    }

    public Order order() {
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        this.order = order;
    }
}
