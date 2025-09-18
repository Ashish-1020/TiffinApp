package com.example.tiffin.dto;

import com.example.tiffin.model.DeliveryStatus;

public class OrderStatusMessageDTO {
    private String orderId;
    private DeliveryStatus status;
    private String message;

    public OrderStatusMessageDTO() {}

    public OrderStatusMessageDTO(String orderId, DeliveryStatus status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
