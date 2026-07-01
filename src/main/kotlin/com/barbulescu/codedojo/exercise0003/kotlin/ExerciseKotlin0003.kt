package com.barbulescu.codedojo.exercise0003.kotlin

class ExerciseKotlin0003 {
    fun create(command: CreateOrder): OrderResult = Accepted(
        Order(
            command.orderId,
            command.customerId,
            command.lines,
            null,
            Confirmed
        )
    )

    fun handle(order: Order, command: OrderCommand): OrderResult {
        return when (command) {
            is AddItem -> addItem(order, command)
            is ApplyDiscount -> applyDiscount(order, command)
            is ShipOrder -> ship(order, command)
            is CancelOrder -> cancel(order, command)
            is ReturnOrder -> returnOrder(order, command)
            is CreateOrder -> "CreateOrder is not a state transition command".asRejected()
        }
    }

    private fun addItem(order: Order, command: AddItem): OrderResult {
        if (order.state !is Confirmed) {
            return "Only confirmed orders can be changed".asRejected()
        }
        val newLine = OrderLine(
            Product(command.productId, command.productName),
            command.quantity,
            command.unitPrice
        )
        return Accepted(order.copy(lines = order.lines + newLine))
    }

    private fun applyDiscount(order: Order, command: ApplyDiscount): OrderResult {
        if (order.state !is Confirmed) {
            return "Only confirmed orders can receive discounts".asRejected()
        }
        return Accepted(order.copy(discount = AppliedDiscount(command.code, command.percent)))
    }

    private fun ship(order: Order, command: ShipOrder): OrderResult {
        if (order.state !is Confirmed) {
            return "Only confirmed orders can be shipped".asRejected()
        }
        return Accepted(order.copy(state = Shipped(command.trackingNumber)))
    }

    private fun cancel(order: Order, command: CancelOrder): OrderResult {
        if (order.state !is Confirmed) {
            return "Only confirmed orders can be cancelled".asRejected()
        }
        return Accepted(order.copy(state = Cancelled(command.reason)))
    }

    private fun returnOrder(order: Order, command: ReturnOrder): OrderResult {
        if (order.state !is Shipped) {
            return "Only shipped orders can be returned".asRejected()
        }
        return Accepted(order.copy(state = Returned(command.reason)))
    }
}
