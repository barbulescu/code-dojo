package com.barbulescu.codedojo.exercise0003;

public sealed interface FulfillmentState permits Confirmed, Shipped, Cancelled, Returned {
}
