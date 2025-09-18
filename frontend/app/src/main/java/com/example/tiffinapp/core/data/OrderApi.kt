package com.example.tiffinapp.core.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface  OrderApi {
    @POST("/api/orders")
    suspend fun placeOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequestDto
    ): Response<OrderResponseDto>
}
