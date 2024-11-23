package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderFileManager {
    private enum OrderColumnName {
        ID("Order ID:"), TIME("Order Time:"), STATUS("Order Status:"), ITEM("Menu Items:");
        private final String columnName;

        OrderColumnName(String columnName){
            this.columnName = columnName;
        }

        public String getColumnName() {
            return this.columnName;
        }
    }
    private final String BASE_DIRECTORY = "orders";
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    public OrderFileManager() {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveOrders(List<Order> orders) {
        String filename = "orders-" + LocalDateTime.now().format(DATE_FORMAT) + ".txt";
        File file = new File(BASE_DIRECTORY, filename);

        lock.writeLock().lock();
        try(PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            for (Order order: orders) {
                writer.println("==== Orders Details ====");
                writer.println(OrderColumnName.ID.getColumnName() + order.getId());
                writer.println(OrderColumnName.TIME.getColumnName() + order.getTime());
                writer.println(OrderColumnName.STATUS.getColumnName()+ order.getStatus());
                writer.println(OrderColumnName.ITEM.getColumnName());
                for (Map.Entry<MenuItem, Integer> item : order.getItems().entrySet()) {
                    writer.printf("%s-%.1f-%d\n", item.getKey().getName(), item.getKey().getPrice(), item.getValue());
                }
                writer.println("========================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Order readOrder() {
        Order currentOrder = null;
        File directory = new File(BASE_DIRECTORY);
        File[] orderFiles = directory.listFiles();

        Arrays.sort(orderFiles, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        File orderFile = orderFiles[0];
        String line;
        lock.readLock().lock();
        try(BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            while((line = reader.readLine()) != null) {
                if(line.startsWith(OrderColumnName.ID.getColumnName())) {
                    String orderIdString = line.substring(OrderColumnName.ID.getColumnName().length()).trim();
                    long orderId = Long.parseLong(orderIdString);
                    currentOrder = new Order(orderId);
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }

        return currentOrder;
    }

}
