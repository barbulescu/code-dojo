package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;

public record AppliedDiscount(String code, BigDecimal percent) {

    public AppliedDiscount {
        validate(code, percent);
    }

    static void validate(String code, BigDecimal percent) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (percent == null) {
            throw new IllegalArgumentException("percent must not be null");
        }
        if (percent.compareTo(BigDecimal.ZERO) <= 0 || percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("percent must be greater than 0 and at most 100");
        }
    }
}
