package com.example.tiffin.dto;



public class CartItemDto {
    private String mealId;
    private int quantity;
    private String name;
    private double price;
    private int offer;

    public CartItemDto(String mealId, int quantity, String name, double price,int offer) {
        this.mealId = mealId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.offer=offer;
    }


    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getMealId() {
        return mealId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}


