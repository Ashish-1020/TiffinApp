package com.example.tiffin.dto;

import java.util.List;

public class MealRequest {
    private String id;
    private String name;
    private double rating;
    private int noofreviews;
    private int price;
    private int offer;
    private String description;
    private String calorie;
    private String dietaryLabel;

    private List<IngredientRequest> ingredients;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNoofreviews() {
        return noofreviews;
    }

    public void setNoofreviews(int noofreviews) {
        this.noofreviews = noofreviews;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getDietaryLabel() {
        return dietaryLabel;
    }

    public void setDietaryLabel(String dietaryLabel) {
        this.dietaryLabel = dietaryLabel;
    }

    public List<IngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }
}


