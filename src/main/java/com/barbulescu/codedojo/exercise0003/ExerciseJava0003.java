package com.barbulescu.codedojo.exercise0003;

import java.util.stream.Stream;

public class ExerciseJava0003 implements OrderWorkflow {
    @Override
    public OrderResult create(CreateOrder command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        Order order = new Order(
                command.orderId(),
                command.customerId(),
                command.lines(),
                null,
                new Confirmed()
        );
        return new Accepted(order);
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
            case CreateOrder ignored -> new Rejected("CreateOrder must be passed to create");
            case AddItem(String productId, String productName, int quantity, Money unitPrice) ->
                    addItem(order, productId, productName, quantity, unitPrice);
            case ApplyDiscount(String code, var percent) -> applyDiscount(order, code, percent);
            case ShipOrder(String trackingNumber) -> ship(order, trackingNumber);
            case CancelOrder(String reason) -> cancel(order, reason);
            case ReturnOrder(String reason) -> returnOrder(order, reason);
        };
    }

    private OrderResult addItem(Order order, String productId, String productName, int quantity, Money unitPrice) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be changed");
        }

        OrderLine newLine = new OrderLine(new Product(productId, productName), quantity, unitPrice);
        return new Accepted(order.withLines(
                Stream.concat(order.lines().stream(), Stream.of(newLine)).toList()
        ));
    }

    private OrderResult applyDiscount(Order order, String code, java.math.BigDecimal percent) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can receive discounts");
        }

        return new Accepted(order.withDiscount(new AppliedDiscount(code, percent)));
    }

    private OrderResult ship(Order order, String trackingNumber) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be shipped");
        }
        if (order.lines().isEmpty()) {
            return new Rejected("Orders without lines cannot be shipped");
        }

        return new Accepted(order.withState(new Shipped(trackingNumber)));
    }

    private OrderResult cancel(Order order, String reason) {
        if (!(order.state() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be cancelled");
        }

        return new Accepted(order.withState(new Cancelled(reason)));
    }

    private OrderResult returnOrder(Order order, String reason) {
        return switch (order.state()) {
            case Shipped(String ignored) -> new Accepted(order.withState(new Returned(reason)));
            case Confirmed ignored -> new Rejected("Only shipped orders can be returned");
            case Cancelled ignored -> new Rejected("Only shipped orders can be returned");
            case Returned ignored -> new Rejected("Only shipped orders can be returned");
        };
    }
}
