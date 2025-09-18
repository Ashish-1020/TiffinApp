package com.example.tiffin.dto;

import java.time.LocalDateTime;


public class TransactionResponseDto {
    private long id;
    private double amount;
    private String type;
    private LocalDateTime timestamp;

    public TransactionResponseDto(long id, double amount, String type, LocalDateTime timestamp) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters
    public long getId() { return id; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

