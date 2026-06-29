package com.barbulescu.codedojo.exercise0003;

import java.util.ArrayList;
import java.util.List;

public class ExerciseJava0003 implements OrderWorkflow {

    @Override
    public OrderResult create(CreateOrder command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        Order order = new Order(
                command.getOrderId(),
                command.getCustomerId(),
                command.getLines(),
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

        if (command instanceof AddItem) {
            AddItem addItem = (AddItem) command;
            return addItem(order, addItem);
        }
        if (command instanceof ApplyDiscount) {
            ApplyDiscount applyDiscount = (ApplyDiscount) command;
            return applyDiscount(order, applyDiscount);
        }
        if (command instanceof ShipOrder) {
            ShipOrder shipOrder = (ShipOrder) command;
            return ship(order, shipOrder);
        }
        if (command instanceof CancelOrder) {
            CancelOrder cancelOrder = (CancelOrder) command;
            return cancel(order, cancelOrder);
        }

        return new Rejected("Unsupported command: " + command.getClass().getSimpleName());
    }

    private OrderResult addItem(Order order, AddItem command) {
        if (!(order.getState() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be changed");
        }

        List<OrderLine> lines = new ArrayList<>(order.getLines());
        lines.add(new OrderLine(
                new Product(command.getProductId(), command.getProductName()),
                command.getQuantity(),
                command.getUnitPrice()
        ));
        order.setLines(lines);
        return new Accepted(order);
    }

    private OrderResult applyDiscount(Order order, ApplyDiscount command) {
        if (!(order.getState() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can receive discounts");
        }

        order.setDiscount(new AppliedDiscount(command.getCode(), command.getPercent()));
        return new Accepted(order);
    }

    private OrderResult ship(Order order, ShipOrder command) {
        if (!(order.getState() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be shipped");
        }
        if (order.getLines().isEmpty()) {
            return new Rejected("Orders without lines cannot be shipped");
        }

        order.setState(new Shipped(command.getTrackingNumber()));
        return new Accepted(order);
    }

    private OrderResult cancel(Order order, CancelOrder command) {
        if (!(order.getState() instanceof Confirmed)) {
            return new Rejected("Only confirmed orders can be cancelled");
        }

        order.setState(new Cancelled(command.getReason()));
        return new Accepted(order);
    }
}
