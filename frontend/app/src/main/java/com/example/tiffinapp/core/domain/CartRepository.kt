package com.example.tiffinapp.core.domain

import com.example.tiffinapp.core.data.CartApi
import com.example.tiffinapp.core.data.CartItemDto
import com.example.tiffinapp.core.data.OrderApi
import com.example.tiffinapp.core.data.OrderRequestDto
import com.example.tiffinapp.core.data.UserDetailApi
import com.example.tiffinapp.core.data.UserDetailDto
import com.example.tiffinapp.core.data.UserDetailFullDto
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartApi: CartApi,
    private val userDetailApi: UserDetailApi,
    private  val orderapi: OrderApi
) {

    suspend fun getCart(token: String): List<CartItemDto> {
        return cartApi.getUserCart("Bearer $token")
    }

    suspend fun addToCart(token: String, mealId: String, quantity: Int) {
        cartApi.addToCart("Bearer $token", mealId, quantity)
    }

    suspend fun removeFromCart(token: String, mealId: String) {
        cartApi.removeFromCart("Bearer $token", mealId)
    }

    suspend fun clearCart(token: String) {
        cartApi.removeAllCartItems("Bearer $token")
    }



        suspend fun getUserDetail(token: String): UserDetailDto {
            return userDetailApi.getDetail("Bearer $token")
        }

    suspend fun getUserFullDetail(token: String): UserDetailFullDto {
        return userDetailApi.getfullDetail("Bearer $token")
    }


        suspend fun updateUserDetail(token: String, phone: String, address: String): UserDetailDto {
            return userDetailApi.updateDetail("Bearer $token", mapOf("phoneNumber" to phone, "address" to address))
        }

        suspend fun createUserDetail(token: String, phone: String, address: String): UserDetailDto {
            return userDetailApi.createDetail("Bearer $token", mapOf("phoneNumber" to phone, "address" to address))
        }

        suspend fun deleteUserDetail(token: String) {
            userDetailApi.deleteDetail("Bearer $token")
        }


    suspend fun placeOrder(token: String, orderRequest: OrderRequestDto) {
        val response = orderapi.placeOrder("Bearer $token", orderRequest)
        if (!response.isSuccessful) throw Exception("Failed to place order: ${response.code()}")
    }



}
