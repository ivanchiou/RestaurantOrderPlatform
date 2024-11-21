package com.ordersystem.model;

public class Order extends AbstractOrder {
    public Order(long id) {
        super(id);
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
