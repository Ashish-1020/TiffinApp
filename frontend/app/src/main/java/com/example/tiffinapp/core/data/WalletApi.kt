package com.example.tiffinapp.core.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WalletApi {
    @GET("wallet/balance")
    suspend fun getBalance(@Header("Authorization") token: String): WalletBalanceResponse

    @POST("wallet/add")
    suspend fun addBalance(
        @Header("Authorization") token: String,
        @Query("amount") amount: Double
    ): AddBalanceResponse

    @GET("wallet/transactions")
    suspend fun getTransactions(@Header("Authorization") token: String): List<TransactionDto>
}