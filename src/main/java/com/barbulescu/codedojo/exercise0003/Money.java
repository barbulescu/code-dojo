package com.barbulescu.codedojo.exercise0003;

import java.math.BigDecimal;

public class Money {

    private BigDecimal amount;
    private String currency;

    public Money(BigDecimal amount, String currency) {
        setAmount(amount);
        setCurrency(currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal amount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String currency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        this.currency = currency;
    }

    public Money plus(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must not be negative");
        }
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    private void ensureSameCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot combine " + currency + " with " + other.currency);
        }
    }
}
