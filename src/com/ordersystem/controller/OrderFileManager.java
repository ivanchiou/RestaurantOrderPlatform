package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;
import com.ordersystem.model.MenuItem;

import java.util.Arrays;
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
        ID("Order ID:"), TIME("Order Time:"), STATUS("Order Status:"), ITEM("Menu Items:"), END("========================");
        private final String columnName;

        OrderColumnName(String columnName){
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return this.columnName;
        }

        public String concat(Object value) {
            return this.toString() + value;
        };
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
                writer.println(OrderColumnName.ID.concat(order.getId()));
                writer.println(OrderColumnName.TIME.concat(order.getTime()));
                writer.println(OrderColumnName.STATUS.concat(order.getStatus()));
                writer.println(OrderColumnName.ITEM);
                for (Map.Entry<MenuItem, Integer> item : order.getItems().entrySet()) {
                    writer.printf("%s-%.1f-%d\n", item.getKey().getName(), item.getKey().getPrice(), item.getValue());
                }
                writer.println(OrderColumnName.END);
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
                if(line.startsWith(OrderColumnName.ID.toString())) {
                    String orderIdString = line.substring((OrderColumnName.ID.toString()).length()).trim();
                    long orderId = Long.parseLong(orderIdString);
                    currentOrder = new Order(orderId);
                } else if(line.startsWith(OrderColumnName.STATUS.toString())) {
                    String orderStatusString = line.substring((OrderColumnName.STATUS.toString()).length()).trim();
                    currentOrder.setStatus(OrderStatus.valueOf(orderStatusString));
                } else if(line.startsWith(OrderColumnName.TIME.toString())) {
                    String orderTimeString = line.substring((OrderColumnName.TIME.toString()).length()).trim();
                    LocalDateTime orderTime = orderTimeString.equals("null") ? null : LocalDateTime.parse(orderTimeString);
                    currentOrder.setTime(orderTime);
                } else if(line.startsWith(OrderColumnName.ITEM.toString())) {
                    while ((line = reader.readLine()) != OrderColumnName.END.toString() && (line = reader.readLine()) != null) {
                        // Menu Items:
                        // 漢堡-50.0-1
                        // 薯條-35.0-1
                        String[] parts = line.split("-");
                        String itemName = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        int quantity = Integer.parseInt(parts[2].trim());
                        MenuItem menuItem = new MenuItem(itemName, price, "");
                        currentOrder.addItem(menuItem, quantity);
                    }
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
