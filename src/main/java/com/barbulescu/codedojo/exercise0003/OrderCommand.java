package com.barbulescu.codedojo.exercise0003;

public sealed interface OrderCommand permits
        CreateOrder,
        AddItem,
        ApplyDiscount,
        ShipOrder,
        CancelOrder,
        ReturnOrder {
}
