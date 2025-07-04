package com.example.tiffin.dto;

public class IngredientRequest {
    private String name;
    private String quantity;

    // Getters and setters

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
