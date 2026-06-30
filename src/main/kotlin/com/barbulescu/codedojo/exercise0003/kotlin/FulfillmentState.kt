package com.barbulescu.codedojo.exercise0003.kotlin

sealed interface FulfillmentState

class Confirmed : FulfillmentState

data class Cancelled(val reason: NonBlankString) : FulfillmentState

data class Shipped(val trackingNumber: NonBlankString) : FulfillmentState

data class Returned(val reason: NonBlankString) : FulfillmentState