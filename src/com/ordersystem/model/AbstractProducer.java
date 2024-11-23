package com.ordersystem.model;
import java.util.ArrayList;
import java.util.List;

public class AbstractProducer {
    protected final List<AbstractOrder> orders;

    public AbstractProducer() {
        this.orders = new ArrayList<>();
    }
    public void addOrder(AbstractOrder order) {
        orders.add(order);
    }

    public boolean cancelOrder(long id) {
        for (AbstractOrder abstractOrder: orders) {
            if (abstractOrder.getId() == id) {
                if (abstractOrder.getClass() == Order.class) {
                    Order order = (Order) abstractOrder;
                    order.cancel();
                }
                return true;
            }
        }
        return false;
    }

    public List<AbstractOrder> getTotalOrders() {
        return this.orders;
    }

    public double getOrderTotalPrice(long id) {
        for (AbstractOrder order: this.orders) {
            if (order.getId() == id) {
                return order.getTotalPrice();
            }
        }
        return 0;
    }

    public double getTotalPrice() {
        double sum = 0;
        for (AbstractOrder order: this.orders) {
            sum += order.getTotalPrice();
        }
        return sum;
    }
}
