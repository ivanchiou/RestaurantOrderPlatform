package com.ordersystem.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.ordersystem.controller.Producer;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order;
import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.OrderFactory;

public class OrderSystemUI extends JFrame {
    private JPanel menuPanel;
    private JPanel orderPanel;
    private JPanel cartListPanel;
    private JSpinner quantitySpinner;
    private JComboBox<MenuItem> itemSelector;
    private JPanel statusPanel;
    private JButton statusWaitingButton;
    private JButton orderButton;
    private JButton addButton;
    private Producer producer;
    private Consumer consumer;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private static final int MAX_IMAGE_SIZE = 100;

    public OrderSystemUI(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
        initializeFrame();
        createJComponents();
        addListeners();
    }

    private void initializeFrame() {
        setTitle("餐廳點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
    }
    
    private void createJComponents() {
        // 建立主要三塊Panels
        this.menuPanel = createMenuPanel();
        this.orderPanel = createOrderPanel();
        this.statusPanel = createStatusPanel();

        add(this.menuPanel, BorderLayout.NORTH);
        add(this.orderPanel, BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(1, Food.values().length, 15, 0));
        panel.setBackground(java.awt.Color.GRAY);
        panel.setBorder(BorderFactory.createTitledBorder("菜單"));

        for(Food food : Food.values()) {
            JPanel menuCardPanel = createMenuCardPanel(food.getName(), food.getFileName(), food.getPrice());
            if (menuCardPanel != null) {
                panel.add(menuCardPanel);
            }
        }

        return panel;
    }

    private JPanel createMenuCardPanel(String name, String filename, double price) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 產生菜單圖片
        JPanel imageItem = new JPanel(new BorderLayout());
        imageItem.setBorder(BorderFactory.createTitledBorder(name));

        String imagePath = "images/" + filename;
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            System.err.println("圖片文件不存在: " + imagePath);
            return null;
        }

        try {
            BufferedImage imageBuffer = ImageIO.read(imageFile);

            // 縮小原始菜單的圖片
            int originalWidth = imageBuffer.getWidth(null);
            int originalHeight = imageBuffer.getHeight(null);

            double scale = Math.min((double)MAX_IMAGE_SIZE/originalWidth, (double)MAX_IMAGE_SIZE/originalHeight);
            int scaleWidth = (int) (originalWidth * scale);
            int scaleHeight = (int) (originalHeight * scale);

            Image scaleImage = imageBuffer.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaleImage));
            imageItem.add(imageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 產生菜單價格
        JLabel priceLabel = new JLabel("價格:"+price);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(imageItem);
        panel.add(priceLabel);

        // 記錄food至menuItem
        this.menuItems.add(new MenuItem(name, price, ""));

        return panel;
    }

    private JPanel createCartItem(MenuItem item, int quantity) {
        JPanel cartItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel(item.getName());
        JLabel quantityLabel = new JLabel("x " + quantity);
        cartItem.add(nameLabel);
        cartItem.add(quantityLabel);

        return cartItem;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(java.awt.Color.DARK_GRAY);

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("選擇餐點"));
        itemSelector = new JComboBox<>(menuItems.toArray(new MenuItem[0]));
        this.quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        
        addButton = new JButton("下單");


        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.X_AXIS));

        selectionPanel.add(itemSelector);
        selectionPanel.add(this.quantitySpinner);
        selectionPanel.add(addButton);
        cartPanel.add(selectionPanel);

        cartListPanel = new JPanel();
        cartListPanel.setBorder(BorderFactory.createTitledBorder("目前訂單已選項目"));
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartPanel.add(cartListPanel);


        panel.add(cartPanel);
        orderButton = new JButton("送出訂單");
        panel.add(orderButton);

        return panel;
    }

    private JPanel createStatusPanel() {
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

        return statusPanel;
    }

    private void addListeners() {
        addButton.addActionListener(e -> {
            MenuItem item = (MenuItem) itemSelector.getSelectedItem();
            this.cartListPanel.add(createCartItem(item, Integer.parseInt(this.quantitySpinner.getValue().toString())));
            revalidate();
            repaint();
        });
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
