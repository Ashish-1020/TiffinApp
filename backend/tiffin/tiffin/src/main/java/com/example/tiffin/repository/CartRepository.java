package com.example.tiffin.repository;


import com.example.tiffin.model.CartItem;
import com.example.tiffin.model.Meal;
import com.example.tiffin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndMeal(User user, Meal meal);
    void deleteByUser(User user);
}

