package com.barbulescu.codedojo.exercise0003.kotlin

sealed interface OrderResult

data class Accepted(val order: Order) : OrderResult

data class Rejected(val reason: NonBlankString) : OrderResult

fun String.asRejected() = Rejected(this.nonBlankString())