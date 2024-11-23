package com.ordersystem.controller;
import com.ordersystem.model.AbstractProducer;
import com.ordersystem.model.AbstractOrder;
import java.util.concurrent.BlockingQueue;

public class Producer extends AbstractProducer {
    private BlockingQueue<AbstractOrder> queue;
    public Producer(BlockingQueue<AbstractOrder> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public void addOrder(AbstractOrder order) {
        super.addOrder(order);
        queue.offer(order);
    }

    public BlockingQueue<AbstractOrder> getQueue() {
        return this.queue;
    }
}
