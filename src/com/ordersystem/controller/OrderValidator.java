package com.ordersystem.controller;
import java.util.regex.Pattern;

public class OrderValidator {
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("^\\d{17}$");
    
    public static boolean isValidOrderID(String orderId) {
        return orderId == null ? false : ORDER_ID_PATTERN.matcher(orderId).matches();
    }
}
