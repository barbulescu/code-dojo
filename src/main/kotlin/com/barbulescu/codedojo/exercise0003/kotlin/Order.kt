package com.barbulescu.codedojo.exercise0003.kotlin

import java.math.BigDecimal

data class AppliedDiscount(val code: NonBlankString, val percent: Percent)

data class Order(
    val orderId: NonBlankString,
    val customerId: NonBlankString,
    val lines: List<OrderLine>,
    val discount: AppliedDiscount?,
    val state: FulfillmentState
) {
    val currency: NonBlankString

    init {
        val currencies = lines.map { it.unitPrice.currency }.distinct()
        require(currencies.size == 1) { "Order must have single currency" }
        currency = currencies.first()
    }

    fun total(): Money {
        if (lines.isEmpty()) {
            return Money(BigDecimal.ZERO, "USD".nonBlankString())
        }

        val subtotal = lines
            .map { it.lineTotal }
            .sumOf { it.amount }

        if (discount == null) {
            return Money(subtotal, currency)
        }

        val multiplier = discount.percent.toMultiplier()
        return Money(subtotal * multiplier, currency)
    }
}
