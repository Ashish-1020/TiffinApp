package com.example.tiffin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Meal {
    @Id
    private String id;

    private String name;
    private String imgurl;
    private double rating;
    private int noofreviews;
    private int price;
    private int offer;
    private String description;
    private String calorie;
    private String dietaryLabel;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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
}

