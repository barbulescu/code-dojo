package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;

public class AppliedDiscount {

    private String code;
    private BigDecimal percent;

    public AppliedDiscount(String code, BigDecimal percent) {
        setCode(code);
        setPercent(percent);
    }

    public String getCode() {
        return code;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        this.code = code;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public BigDecimal percent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        if (percent == null) {
            throw new IllegalArgumentException("percent must not be null");
        }
        if (percent.compareTo(BigDecimal.ZERO) <= 0 || percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("percent must be greater than 0 and at most 100");
        }
        this.percent = percent;
    }
}
