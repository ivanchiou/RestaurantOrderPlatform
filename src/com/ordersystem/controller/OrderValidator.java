package com.ordersystem.controller;
import java.util.regex.Pattern;

public class OrderValidator {
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("^\\d{17}$");

    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    private static final Pattern QUANTITY_PATTERN = Pattern.compile("^[1-9][0-9]?$"); // (1~99)
    
    public static boolean isValidOrderID(String orderId) {
        return orderId == null ? false : ORDER_ID_PATTERN.matcher(orderId).matches();
    }

    public static boolean isValidPrice(String price) {
        return price == null ? false: PRICE_PATTERN.matcher(price).matches();
    }

    public static boolean isValidQuantity(String quantity) {
        return quantity == null ? false : QUANTITY_PATTERN.matcher(quantity).matches();
    }
}
