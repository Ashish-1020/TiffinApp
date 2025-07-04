package com.example.tiffin.repository;

import com.example.tiffin.model.Ingredient;
import com.example.tiffin.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByMeal(Meal meal);
}
