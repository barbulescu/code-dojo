package com.barbulescu.codedojo.exercise0003;

public class CancelOrder implements OrderCommand {

    private String reason;

    public CancelOrder(String reason) {
        setReason(reason);
    }

    public String getReason() {
        return reason;
    }

    public String reason() {
        return reason;
    }

    public void setReason(String reason) {
        if (isBlank(reason)) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        this.reason = reason;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
