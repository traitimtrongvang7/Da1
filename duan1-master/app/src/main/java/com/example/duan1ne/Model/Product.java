package com.example.duan1ne.Model;

public class Product {
    private int id;
    private String name;
    private int price;

    private boolean inCart;

    public Product(int id, String name, int price, boolean inCart) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inCart = inCart;
    }

    public Product(int id, String name, byte[] image, int price, int inCart, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }
}