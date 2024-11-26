package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.MenuItem;
import java.util.Random;
public class OrderFactory {
    private final static Random random = new Random();
    public static Order createNextOrder(MenuItem item, int quantity) {
        if (OrderValidator.isValidQuantity(quantity+"")) {
            long id = Long.parseLong(System.currentTimeMillis() + String.format("%04d", random.nextInt(10000))); // 範圍0~9999
            Order order = new Order(id);
            order.addItem(item, quantity);
            return order;
        }
        return null;
    }
}
