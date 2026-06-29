package com.barbulescu.codedojo.exercise0003;

public class AddItem implements OrderCommand {

    private String productId;
    private String productName;
    private int quantity;
    private Money unitPrice;

    public AddItem(String productId, String productName, int quantity, Money unitPrice) {
        setProductId(productId);
        setProductName(productName);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    public String getProductId() {
        return productId;
    }

    public String productId() {
        return productId;
    }

    public void setProductId(String productId) {
        if (isBlank(productId)) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public String productName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (isBlank(productName)) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        this.productName = productName;
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

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
