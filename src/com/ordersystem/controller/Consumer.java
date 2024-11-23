package com.ordersystem.controller;
import com.ordersystem.model.AbstractConsumer;
import com.ordersystem.model.AbstractOrder;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;

import java.util.concurrent.BlockingQueue;

public class Consumer extends AbstractConsumer implements Runnable {
    private BlockingQueue<AbstractOrder> queue;
    public Consumer(BlockingQueue<AbstractOrder> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                AbstractOrder order = this.queue.take(); // blocking
                boolean isProcessed = processOrder(order.getId());
                Thread.sleep(1000);
                if (isProcessed == true) {
                    completedOrder(order);
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean processOrder(long id) {
        System.out.println("訂單正在處理中..." + id);
        return super.processOrder(id);
    }

    @Override
    public void completedOrder(AbstractOrder order) {
        System.out.println("訂單已完成" + order.getId());
        super.completedOrder(order);
    }

    public Order getProcessingOrder() {
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PROCESSING) {
                return order;
            }
        }
        return null;
    }

}
