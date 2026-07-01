package com.barbulescu.codedojo.exercise0003.kotlin

import java.math.BigDecimal

data class Money(val amount: BigDecimal, val currency: NonBlankString) {

    init {
        require(amount >= BigDecimal.ZERO) { "Amount must not be negative" }
    }

    operator fun plus(other: Money): Money {
        ensureSameCurrency(other)
        return Money(amount + other.amount, currency)
    }

    operator fun minus(other: Money): Money {
        ensureSameCurrency(other)
        return Money(amount - other.amount, currency)
    }

    private fun ensureSameCurrency(other: Money) {
        require(currency == other.currency) { """Cannot combine $currency with ${other.currency}""" }
    }

    fun multiply(multiplier: Int): Money {
        require(multiplier >= 0) { "multiplier must not be negative" }
        return Money(amount.multiply(multiplier.toBigDecimal()), currency)
    }
}
