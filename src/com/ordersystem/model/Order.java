package com.ordersystem.model;

import java.time.LocalDateTime;

public class Order extends AbstractOrder {
    private LocalDateTime time;
    public Order(long id) {
        super(id);
        this.time = LocalDateTime.now();
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
