package com.example.demo.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ReportDaily {

    @JsonInclude(value = Include.NON_NULL)
    private int totalOrders;
    @JsonInclude(value = Include.NON_NULL)
    private int totalProducts;
    @JsonInclude(value = Include.NON_NULL)
    private Long totalRevenue;
    @JsonInclude(value = Include.NON_NULL)

    private int totalPromotion;

    // Constructor with all parameters
    public ReportDaily(int totalOrders, int totalProducts, Long totalRevenue, int totalPromotion) {
        this.totalOrders = totalOrders;
        this.totalProducts = totalProducts;
        this.totalRevenue = totalRevenue;
        this.totalPromotion = totalPromotion;
    }
    public ReportDaily() {
    }


    // Getters and setters
    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalPromotion() {
        return totalPromotion;
    }

    public void setTotalPromotion(int totalPromotion) {
        this.totalPromotion = totalPromotion;
    }
}
