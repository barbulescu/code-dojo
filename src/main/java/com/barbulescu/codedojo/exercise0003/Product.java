package com.barbulescu.codedojo.exercise0003;

public record Product(String id, String name) {
    public Product {
        if (isBlank(id)) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (isBlank(name)) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
