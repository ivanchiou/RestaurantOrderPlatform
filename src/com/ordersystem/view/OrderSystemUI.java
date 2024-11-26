package com.ordersystem.view;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.ordersystem.controller.Producer;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order;
import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.OrderFactory;

public class OrderSystemUI extends JFrame {
    private JButton statusWaitingButton;  
    public OrderSystemUI(Producer producer, Consumer consumer) {
        setSize(800, 600);
        setTitle("餐廳點餐系統");
        setLayout(new BorderLayout());

        JPanel menuItemPanel = new JPanel();
        menuItemPanel.setBackground(java.awt.Color.GRAY);
        add(menuItemPanel, BorderLayout.NORTH);

        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(java.awt.Color.DARK_GRAY);
        JButton orderButton = new JButton("下單");
        orderPanel.add(orderButton);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuItem item = new MenuItem("漢堡", 50.0, "美味漢堡");
                Order order = OrderFactory.createNextOrder(item, 1);
                producer.addOrder(order);
                System.out.println(order.getId());
                orderButton.setEnabled(false);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                orderButton.setEnabled(true);
            }
        });
        add(orderPanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(java.awt.Color.LIGHT_GRAY);
        statusPanel.setBounds(25, 400, 650, 200);

        JLabel statusText = new JLabel("訂單狀態");
        statusWaitingButton = new JButton();
        JButton statusProcessingButton = new JButton("訂單正在準備中:"+(consumer.getProcessingOrder() == null ? 0 : 1));
        JButton statusCompletedButton = new JButton("餐點已完成:"+consumer.getCountOfCompletedOrders());

        statusPanel.add(statusText);
        statusPanel.add(statusWaitingButton);
        statusPanel.add(statusProcessingButton);
        statusPanel.add(statusCompletedButton);

        Thread queuemonitor = new Thread(()->{
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    statusWaitingButton.setText("訂單已接收:"+producer.getTotalOrders().size());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        queuemonitor.start();

        add(statusPanel, BorderLayout.SOUTH);
    }
}
