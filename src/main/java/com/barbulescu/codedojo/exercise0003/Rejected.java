package com.barbulescu.codedojo.exercise0003;

public record Rejected(String reason) implements OrderResult {
    public Rejected {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
    }
}
