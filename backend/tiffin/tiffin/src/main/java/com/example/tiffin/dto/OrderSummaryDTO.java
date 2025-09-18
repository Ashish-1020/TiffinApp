package com.example.tiffin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDTO {
    private String orderId;
    private LocalDateTime time;
    private List<String> meals; // Parsed from JSON
    private BigDecimal totalCost;
    private String deliveryStatus;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(String orderId, LocalDateTime time, List<String> meals, BigDecimal totalCost, String deliveryStatus) {
        this.orderId = orderId;
        this.time = time;
        this.meals = meals;
        this.totalCost = totalCost;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public List<String> getMeals() { return meals; }
    public void setMeals(List<String> meals) { this.meals = meals; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
}
