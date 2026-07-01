package com.barbulescu.codedojo.exercise0003.kotlin

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode

@JvmInline
value class NonBlankString(val value: String) {
    init {
        require(value.isNotBlank()) { "value must not be blank" }
    }
}

@JvmInline
value class Percent(val value: BigDecimal) {
    init {
        require(value > ZERO && value <= BigDecimal.valueOf(100)) { "percent must be greater than 0 and at most 100" }
    }

    fun toMultiplier(): BigDecimal = BigDecimal.ONE - value.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN)
}

@JvmInline
value class PositiveInt(val value: Int) {
    init {
        require(value > 0) { "value must be positive" }
    }
}

fun String.nonBlankString(): NonBlankString = NonBlankString(this)
fun Int.positiveInt(): PositiveInt = PositiveInt(this)
fun String.percent(): Percent = Percent(this.toBigDecimal())

data class Product(val id: NonBlankString, val name: NonBlankString)