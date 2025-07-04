package com.example.tiffin.dto;



public class CartItemDto {
    private String mealId;
    private int quantity;

    public CartItemDto(String mealId, int quantity) {
        this.mealId = mealId;
        this.quantity = quantity;
    }

    public String getMealId() {
        return mealId;
    }

    public int getQuantity() {
        return quantity;
    }
}

