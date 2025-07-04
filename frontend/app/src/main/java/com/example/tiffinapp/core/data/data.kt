package com.example.tiffinapp.core.data

data class LoginResponse(
    val id: Int,
    val message: String,
    val status: Boolean,
    val username: String,
    val token: String
)

data class LoginRequest(
    val email: String,
    val password: String
)


data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)


data class SignupResponse(
    val message: String,
    val status: Boolean
)


data class IngredientRequest(
    val name: String,
    val quantity: String
)

data class MealRequest(
    val id: String,
    val name: String,
    val rating: Double,
    val noofreviews: Int,
    val price: Int,
    val offer: Int,
    val description: String,
    val calorie: String,
    val dietaryLabel: String,
    val ingredients: List<IngredientRequest>
)

data class MealResponse(
    val id: String,
    val imgurl:String,
    val name: String,
    val rating: Double,
    val noofreviews: Int,
    val price: Int,
    val offer: Int,
    val description: String,
    val calorie: String,
    val dietaryLabel: String,
    val ingredients: List<IngredientRequest>
)
