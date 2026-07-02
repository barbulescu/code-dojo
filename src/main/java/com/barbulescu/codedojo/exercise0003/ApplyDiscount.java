package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;

public record ApplyDiscount(String code, BigDecimal percent) implements OrderCommand {
    public ApplyDiscount {
        AppliedDiscount.validate(code, percent);
    }
}
