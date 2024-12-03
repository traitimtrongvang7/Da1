package com.example.duan1ne.Model;

public class User {

    private String id;
    private String name;
    private int role;
    private String email;
    private String phone;
    private String address;

    // Default constructor
    public User() {
    }

    // Constructor with all fields
    public User(String id, String name, int role, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getter and setter methods (improved readability)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}