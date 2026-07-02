package com.barbulescu.codedojo.exercise0003;

public interface OrderWorkflow {
    OrderResult create(CreateOrder command);
    OrderResult handle(Order order, OrderCommand command);
}
