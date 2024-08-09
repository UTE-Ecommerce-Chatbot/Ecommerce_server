package com.example.demo.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long productId, Long colorId) {
        super("Inventory not found for product ID " + productId + " and color ID " + colorId);
    }
}