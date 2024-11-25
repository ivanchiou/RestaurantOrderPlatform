package com.ordersystem.model;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOrder implements InterfaceOrder {
    protected final long id;
    protected OrderStatus status;
    protected Map<MenuItem, Integer> items;

    public AbstractOrder(long id) {
        this.id = id;
        this.status = OrderStatus.WAITING;
        this.items = new HashMap<>();
    }
    
    @Override
    public long getId() {
        return this.id;
    }
    
    @Override
    public Map<MenuItem, Integer> getItems() {
        return this.items;
    }
    
    @Override
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public OrderStatus getStatus() {
        return this.status;
    }

    @Override
    public void addItem(MenuItem item, int quantity) {
        this.items.put(item, this.items.getOrDefault(item, 0) + quantity);
    }

    @Override
    public Map.Entry<MenuItem, Integer> getItem(String name) {
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            if(entry.getKey().getName().equals(name)) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public double getTotalPrice() {
        double sum = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            sum += entry.getKey().getPrice() * entry.getValue();
        }

        return sum;
    }
}
