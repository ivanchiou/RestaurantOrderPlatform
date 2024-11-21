package com.ordersystem.model;
import java.util.Map;

public interface InterfaceOrder {
    long getId();
    Map<MenuItem, Integer> getItems();
    OrderStatus getStatus();
    void setStatus(OrderStatus status);
    void addItem(MenuItem item, int quantity);
    Map.Entry<MenuItem, Integer> getItem(String name);
    double getTotalPrice();
}
