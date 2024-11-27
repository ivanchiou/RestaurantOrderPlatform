package com.ordersystem.view;

public enum Food {
    BURGER("漢堡", 50, "burger.jpg"),
    FRIES("薯條", 20, "fries.jpg"),
    COFFEE("咖啡", 40, "coffee.jpg"),
    COLA("可樂", 25, "cola.jpg");

    private final String name;
    private final double price;
    private final String filename;

    Food(String name, double price, String filename) {
        this.name = name;
        this.price = price;
        this.filename = filename;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getFileName() {
        return this.filename;
    }
}
