package com.example.tiffinapp.core.data

import kotlinx.serialization.Serializable


@Serializable
data class CartItemDto(
    val mealId: String,
    val quantity: Int,
    val name: String,
    val price: Double,
    val offer: Int
)

