package com.barbulescu.codedojo.exercise0003;

public class Cancelled implements FulfillmentState {

    private String reason;

    public Cancelled(String reason) {
        setReason(reason);
    }

    public String getReason() {
        return reason;
    }

    public String reason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        this.reason = reason;
    }
}
