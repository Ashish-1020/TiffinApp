package com.example.tiffin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_details")
public class UserDetail {

    @Id
    private Long userId;

    private String phoneNumber;
    private String address;

    @OneToOne
    @MapsId // This maps the primary key of this entity to the user’s ID
    @JoinColumn(name = "user_id")
    private User user;

    // Getters, Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

