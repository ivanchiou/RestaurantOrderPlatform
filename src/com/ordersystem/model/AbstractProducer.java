package com.ordersystem.model;
import java.util.List;

public class AbstractProducer {
    protected List<Order> orders;

    public AbstractProducer() {
        
    }
    public void addOrder(Order order) {
        orders.add(order);
    }

    public boolean cancelOrder(long id) {
        for (Order order: orders) {
            if (order.getId() == id) {
                order.cancel();
                return true;
            }
        }
        return false;
    }

    public List<Order> getTotalOrders() {
        return this.orders;
    }

    public double getOrderTotalPrice(long id) {
        for (Order order: orders) {
            if (order.getId() == id) {
                return order.getTotalPrice();
            }
        }
        return 0;
    }

    public double getTotalPrice() {
        double sum = 0;
        for (Order order: orders) {
            sum += order.getTotalPrice();
        }
        return sum;
    }
}
