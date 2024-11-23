package com.ordersystem.model;
import java.util.ArrayList;
import java.util.List;
public class AbstractConsumer {
    protected final List<Order> orders;

    public AbstractConsumer() {
        this.orders = new ArrayList<>();
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

    public void completedOrder(AbstractOrder order) {
        order.setStatus(OrderStatus.COMPLETED);
    }

    public int getCountOfCompletedOrders() {
        int count = 0;
        for (Order order: orders) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                count++;
            }
        }
        return count;
    }

    public List<Order> getCompletedOrders() {
        List<Order> completedList = new ArrayList<>();
        for (Order order: orders) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                completedList.add(order);
            }
        }

        return completedList;
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
