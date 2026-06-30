package com.barbulescu.codedojo.exercise0003.kotlin

data class OrderLine(val product: Product, val quantity: PositiveInt, val unitPrice: Money) {
    val lineTotal: Money = unitPrice.multiply(quantity.value)
}
