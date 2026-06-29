package com.barbulescu.codedojo.exercise0003;

import java.util.ArrayList;

public class ExerciseJava0003 implements OrderWorkflow {

    @Override
    public OrderResult create(CreateOrder command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }
        return new Accepted(new Order(
                command.orderId(),
                command.customerId(),
                command.lines(),
                null,
                new Confirmed()
        ));
    }

    @Override
    public OrderResult handle(Order order, OrderCommand command) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        return switch (command) {
            case AddItem addItem -> addItem(order, addItem);
            case ApplyDiscount applyDiscount -> applyDiscount(order, applyDiscount);
            case ShipOrder shipOrder -> ship(order, shipOrder);
            case CancelOrder cancelOrder -> cancel(order, cancelOrder);
            case ReturnOrder returnOrder -> returnOrder(order, returnOrder);
            case CreateOrder createOrder -> new Rejected("CreateOrder is not a state transition command");
        };
    }

    private OrderResult addItem(Order order, AddItem command) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be changed");
        }
        var newLines = new ArrayList<>(order.lines());
        newLines.add(new OrderLine(
                new Product(command.productId(), command.productName()),
                command.quantity(),
                command.unitPrice()
        ));
        return new Accepted(new Order(order.orderId(), order.customerId(), newLines, order.discount(), order.state()));
    }

    private OrderResult applyDiscount(Order order, ApplyDiscount command) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can receive discounts");
        }
        return new Accepted(new Order(
                order.orderId(), order.customerId(), order.lines(),
                new AppliedDiscount(command.code(), command.percent()),
                order.state()
        ));
    }

    private OrderResult ship(Order order, ShipOrder command) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be shipped");
        }
        if (order.lines().isEmpty()) {
            return new Rejected("Orders without lines cannot be shipped");
        }
        return new Accepted(new Order(
                order.orderId(), order.customerId(), order.lines(),
                order.discount(), new Shipped(command.trackingNumber())
        ));
    }

    private OrderResult cancel(Order order, CancelOrder command) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be cancelled");
        }
        return new Accepted(new Order(
                order.orderId(), order.customerId(), order.lines(),
                order.discount(), new Cancelled(command.reason())
        ));
    }

    private OrderResult returnOrder(Order order, ReturnOrder command) {
        if (!(order.state() instanceof Shipped)) {
            return new Rejected("Only shipped orders can be returned");
        }
        return new Accepted(new Order(
                order.orderId(), order.customerId(), order.lines(),
                order.discount(), new Returned(command.reason())
        ));
    }
}
