package com.example.tiffin.repository;

import com.example.tiffin.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, String> {
    Optional<Meal> findById(String id);
}