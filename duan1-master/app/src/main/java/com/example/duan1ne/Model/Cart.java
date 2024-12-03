package com.example.duan1ne.Model;

public class Cart {
    private int id;
    private int productId;
    private String name;
    private double price;  // Use double for price to handle decimals
    private int quantity;

    public Cart(int id, int productId, String name, double price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        // You can add validation here (e.g., if (quantity >= 0))
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    // Optional methods:
    // - Check if product exists in cart
    // - Update quantity of existing product
}