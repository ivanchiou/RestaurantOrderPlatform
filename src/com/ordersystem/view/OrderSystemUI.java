package com.ordersystem.view;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.ordersystem.controller.Producer;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order;
import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.OrderFactory;

public class OrderSystemUI extends JFrame {
    private JPanel menuPanel;
    private JPanel cartPanel;
    private JPanel cartListPanel;
    private Map<MenuItem, Integer> cartListItems;
    private JPanel statusPanel;
    private JComboBox<MenuItem> tableSelector;
    private JButton submitButton;
    private JButton processButton;
    private JButton addButton;
    private JSpinner quantitySpinner;
    private Producer producer;
    private Consumer consumer;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private static final int MAX_ICON_SIZE = 90;

    public OrderSystemUI(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
        this.cartListItems = new HashMap<>();
        initializeFrame();
        createComponents();
        layoutComponents();
        addListeners();
    }

    private void initializeFrame() {
        setTitle("餐廳點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        // 創建主要面板
        this.menuPanel = createMenuPanel();
        this.cartPanel = createCartPanel();
        this.statusPanel = createStatusPanel();
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("菜單"));

        // 創建菜品卡片面板
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        
        // 添加菜品卡片
        Food[] foods = Food.values(); 
        for(Food food : foods) {
            cardsPanel.add(createFoodCard(food.getName(), food.getPrice(), food.getFileName()));
        }

        panel.add(cardsPanel);
        return panel;
    }

    private JPanel createFoodCard(String name, double price, String filename) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 食物圖片
        JPanel imageItem = new JPanel(new BorderLayout());
        imageItem.setBorder(BorderFactory.createTitledBorder(name));
        try {
            String imagePath = "images/" + filename;
            File imageFile = new File(imagePath);
            
            if (!imageFile.exists()) {
                System.err.println("圖片文件不存在: " + imagePath);
                return null;
            }
            BufferedImage imageBuffer = ImageIO.read(imageFile);
            int originalWidth = imageBuffer.getWidth(null);
            int originalHeight = imageBuffer.getHeight(null);

            double scale = Math.min((double) MAX_ICON_SIZE / originalWidth, (double) MAX_ICON_SIZE / originalHeight);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            Image scaledImg = imageBuffer.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageItem.add(imageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 價格標籤
        JLabel priceLabel = new JLabel("價格:"+price);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(imageItem);
        card.add(Box.createVerticalStrut(10));
        card.add(priceLabel);

        this.menuItems.add(new MenuItem(name, price, ""));

        return card;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // 選擇餐點區域
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableSelector = new JComboBox<>(menuItems.toArray(new MenuItem[0]));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        addButton = new JButton("下單");
        
        selectionPanel.add(tableSelector);
        selectionPanel.add(quantitySpinner);
        selectionPanel.add(addButton);

        // 購物車列表
        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        // 送出訂單按鈕
        submitButton = new JButton("送出訂單");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 處理訂單按鈕
        processButton = new JButton("處理訂單");
        processButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(submitButton);
        buttonPanel.add(processButton);

        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        orderPanel.add(selectionPanel);
        orderPanel.add(new JScrollPane(cartListPanel));

        panel.add(orderPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createCartItem(MenuItem item, int quantity) {
        String name = item.getName();
        //增加item到MenuItem Map
        this.cartListItems.put(item, quantity);

        JPanel cartItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel imgLabel = new JLabel("IMG");
        imgLabel.setPreferredSize(new Dimension(30, 30));
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.LIGHT_GRAY);
        
        JLabel nameLabel = new JLabel(name);
        JLabel quantityLabel = new JLabel("x" + quantity);
        JButton deleteButton = new JButton("remove");
        deleteButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setPreferredSize(new Dimension(50, 20));

        cartItem.add(imgLabel);
        cartItem.add(nameLabel);
        cartItem.add(quantityLabel);
        cartItem.add(deleteButton);

        return cartItem;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("訂單狀態"));

        // 狀態顯示區域
        JPanel statusDisplay = new JPanel(new GridLayout(1, 3, 0, 10));
        
        // 訂單狀態項目
        statusDisplay.add(createStatusItem("訂單已接收: "+this.producer.getTotalOrders().size(), 
            "更新時間: " + getCurrentTime()));
        statusDisplay.add(createStatusItem("正在準備中: "+(this.consumer.getProcessingOrder() == null ? 0: 1), 
            "更新時間: " + getCurrentTime()));
        statusDisplay.add(createStatusItem("餐點已完成: "+this.consumer.getCountOfCompletedOrders(), 
            "更新時間: " + getCurrentTime()));

        panel.add(Box.createVerticalStrut(10));
        panel.add(statusDisplay);

        return panel;
    }

    private JPanel createStatusItem(String status, String time) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JLabel statusLabel = new JLabel(status);
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(timeLabel.getFont().deriveFont(10f));
        
        item.add(statusLabel, BorderLayout.CENTER);
        item.add(timeLabel, BorderLayout.SOUTH);
        
        return item;
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // 添加主要元件到窗口
        add(menuPanel, BorderLayout.NORTH);
        add(cartPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    // 強制更新 UI 的方法
    private void forceUpdateUI() {
        // 更新購物車面板
        cartListPanel.revalidate();
        cartListPanel.repaint();
        
        // 更新整個視窗
        revalidate();
        repaint();
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)tableSelector.getSelectedItem();
                cartListPanel.add(createCartItem(item, Integer.parseInt(quantitySpinner.getValue().toString())));
                forceUpdateUI();
            }
        });

        submitButton.addActionListener(e -> {
            // 實現送出訂單的邏輯
            Order order = null;
            for (Map.Entry<MenuItem, Integer> item : this.cartListItems.entrySet()) {
                if (order == null) {
                    order = OrderFactory.createNextOrder(item.getKey(), item.getValue());
                } else {
                    order.addItem(item.getKey(), item.getValue());
                }
            }

            if (order != null) {
                this.producer.addOrder(order);
                System.out.println(order.getId());
            }
            submitButton.setEnabled(false);
            try {
                this.cartListItems = new HashMap<>();
                this.cartListPanel.removeAll();
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            submitButton.setEnabled(true);
        });

        processButton.addActionListener(e -> {
            // 實現處理訂單的邏輯
            JOptionPane.showMessageDialog(this, "開始處理訂單");
        });

        Thread queuemonitor = new Thread(()->{
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    Component[] components = statusPanel.getComponents();
                    JPanel statusDisplay = (JPanel) components[1];
                    JPanel waitingStatusPanel = (JPanel) statusDisplay.getComponents()[0];
                    JLabel waitingStatusLabel = (JLabel) waitingStatusPanel.getComponents()[0];
                    waitingStatusLabel.setText("訂單已接收: "+this.producer.getTotalOrders().size());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        queuemonitor.start();
    }
}
