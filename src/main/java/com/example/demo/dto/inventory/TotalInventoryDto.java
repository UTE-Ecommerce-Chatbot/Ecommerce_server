package com.example.demo.dto.inventory;

public class TotalInventoryDto {
    private Long productId;
    private Integer totalInventory;

    public TotalInventoryDto(Long productId, Integer totalInventory) {
        this.productId = productId;
        this.totalInventory = totalInventory;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Integer totalInventory) {
        this.totalInventory = totalInventory;
    }
}