package com.ordersystem.model;

public class MenuItem {
    private String name;
    private String description;
    private double price;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}
