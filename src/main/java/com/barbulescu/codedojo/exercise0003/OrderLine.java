package com.barbulescu.codedojo.exercise0003;

public class OrderLine {

    private Product product;
    private int quantity;
    private Money unitPrice;

    public OrderLine(Product product, int quantity, Money unitPrice) {
        setProduct(product);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    public Product getProduct() {
        return product;
    }

    public Product product() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int quantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.quantity = quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice must not be null");
        }
        this.unitPrice = unitPrice;
    }

    public Money lineTotal() {
        return unitPrice.multiply(quantity);
    }
}
