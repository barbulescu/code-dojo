package com.barbulescu.codedojo.exercise0003;

public class Product {

    private String id;
    private String name;

    public Product(String id, String name) {
        setId(id);
        setName(name);
    }

    public String getId() {
        return id;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
