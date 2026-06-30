package com.barbulescu.codedojo.exercise0003.kotlin


sealed interface OrderCommand

data class AddItem(
    val productId: NonBlankString,
    val productName: NonBlankString,
    val quantity: PositiveInt,
    val unitPrice: Money
) : OrderCommand

data class ApplyDiscount(val code: NonBlankString, val percent: Percent) : OrderCommand

data class CancelOrder(val reason: NonBlankString) : OrderCommand

data class CreateOrder(val orderId: NonBlankString, val customerId: NonBlankString, val lines: List<OrderLine>) : OrderCommand {
    init {
        require(lines.isNotEmpty()) { "lines must not be empty" }
    }
}

data class ShipOrder(val trackingNumber: NonBlankString) : OrderCommand

data class ReturnOrder(val reason: NonBlankString) : OrderCommand
