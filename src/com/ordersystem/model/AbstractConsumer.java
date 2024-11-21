package com.ordersystem.model;
import java.util.List;
public class AbstractConsumer {
    protected List<Order> orders;

    public AbstractConsumer() {
        
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getTotalOrders() {
        return this.orders;
    }

    public boolean processOrder(long id) {
        for (Order order: orders) {
            if (order.getStatus() == OrderStatus.PROCESSING) {
                return false;
            } else if (order.getId() == id) {
                order.setStatus(OrderStatus.PROCESSING);
                return true;
            }
        }

        return false;
    }

    public void completedOrder(Order order) {
        order.setStatus(OrderStatus.COMPLETED);
    }

    public int getCompletedOrders() {
        int count = 0;
        for (Order order: orders) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                count++;
            }
        }
        return count;
    }

    public double getRevenue() {
        double sum = 0;
        for (Order order: orders) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                sum += order.getTotalPrice();
            }
        }
        return sum;
    }
}
