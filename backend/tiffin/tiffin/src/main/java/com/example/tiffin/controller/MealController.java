package com.example.tiffin.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.tiffin.dto.IngredientRequest;
import com.example.tiffin.dto.MealRequest;
import com.example.tiffin.model.Ingredient;
import com.example.tiffin.model.Meal;
import com.example.tiffin.repository.IngredientRepository;
import com.example.tiffin.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMeal(
            @RequestPart("image") MultipartFile file,
            @RequestPart("meal") MealRequest mealRequest) throws IOException {

        System.out.println("🚀 Received uploadMeal request");

        // 1. Upload image to Cloudinary
        System.out.println("🖼️ Uploading image to Cloudinary...");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadResult.get("secure_url").toString();
        System.out.println("✅ Image uploaded. URL: " + imageUrl);

        // 2. Save Meal
        Meal meal = new Meal();
        meal.setId(mealRequest.getId());
        meal.setName(mealRequest.getName());
        meal.setImgurl(imageUrl);
        meal.setRating(mealRequest.getRating());
        meal.setNoofreviews(mealRequest.getNoofreviews());
        meal.setPrice(mealRequest.getPrice());
        meal.setOffer(mealRequest.getOffer());
        meal.setDescription(mealRequest.getDescription());
        meal.setCalorie(mealRequest.getCalorie());
        meal.setDietaryLabel(mealRequest.getDietaryLabel());

        System.out.println("📦 Saving Meal: " + meal.getName() + " | ID: " + meal.getId());
        mealRepository.save(meal);
        System.out.println("✅ Meal saved successfully");

        // 3. Save Ingredients
        System.out.println("🧾 Saving ingredients:");
        for (IngredientRequest ir : mealRequest.getIngredients()) {
            Ingredient ing = new Ingredient();
            ing.setName(ir.getName());
            ing.setQuantity(ir.getQuantity());
            ing.setMeal(meal);

            System.out.println("🔹 " + ir.getName() + " - " + ir.getQuantity());
            ingredientRepository.save(ing);
        }

        System.out.println("✅ All ingredients saved successfully");
        return ResponseEntity.ok("Meal uploaded successfully!");
    }




    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMeals() {
        List<Meal> meals = mealRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Meal meal : meals) {
            Map<String, Object> mealData = new HashMap<>();
            mealData.put("id", meal.getId());
            mealData.put("name", meal.getName());
            mealData.put("imgurl", meal.getImgurl());
            mealData.put("rating", meal.getRating());
            mealData.put("noofreviews", meal.getNoofreviews());
            mealData.put("price", meal.getPrice());
            mealData.put("offer", meal.getOffer());
            mealData.put("description", meal.getDescription());
            mealData.put("calorie", meal.getCalorie());
            mealData.put("dietaryLabel", meal.getDietaryLabel());

            List<Ingredient> ingredients = ingredientRepository.findByMeal(meal);
            List<Map<String, String>> ingredientList = new ArrayList<>();
            for (Ingredient ing : ingredients) {
                Map<String, String> ingData = new HashMap<>();
                ingData.put("name", ing.getName());
                ingData.put("quantity", ing.getQuantity());
                ingredientList.add(ingData);
            }

            mealData.put("ingredients", ingredientList);
            response.add(mealData);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMealById(@PathVariable String id) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);
        System.out.println("meal with id called"+id);
        if (optionalMeal.isEmpty()) {
            return ResponseEntity.status(404).body("Meal not found with ID: " + id);
        }

        Meal meal = optionalMeal.get();

        // Prepare response
        Map<String, Object> mealData = new HashMap<>();
        mealData.put("id", meal.getId());
        mealData.put("name", meal.getName());
        mealData.put("imgurl", meal.getImgurl());
        mealData.put("rating", meal.getRating());
        mealData.put("noofreviews", meal.getNoofreviews());
        mealData.put("price", meal.getPrice());
        mealData.put("offer", meal.getOffer());
        mealData.put("description", meal.getDescription());
        mealData.put("calorie", meal.getCalorie());
        mealData.put("dietaryLabel", meal.getDietaryLabel());

        List<Ingredient> ingredients = ingredientRepository.findByMeal(meal);
        List<Map<String, String>> ingredientList = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            Map<String, String> ingData = new HashMap<>();
            ingData.put("name", ing.getName());
            ingData.put("quantity", ing.getQuantity());
            ingredientList.add(ingData);
        }

        mealData.put("ingredients", ingredientList);


        return ResponseEntity.ok(mealData);
    }




}

