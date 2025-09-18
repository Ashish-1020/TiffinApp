package com.example.tiffinapp.core.data

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApi {
    @GET("cart")
    suspend fun getUserCart(
        @Header("Authorization") token: String
    ): List<CartItemDto>

    @POST("cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Query("mealId") mealId: String,
        @Query("quantity") quantity: Int
    )

    @DELETE("cart/remove")
    suspend fun removeFromCart(
        @Header("Authorization") token: String,
        @Query("mealId") mealId: String
    )

    @DELETE("cart/removeAll")
    suspend fun removeAllCartItems(
        @Header("Authorization") token: String
    )
}