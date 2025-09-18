package com.example.tiffinapp.core.domain

import com.example.tiffinapp.core.data.AddBalanceResponse
import com.example.tiffinapp.core.data.TransactionDto
import com.example.tiffinapp.core.data.WalletApi
import com.example.tiffinapp.wallet.TransactionType
import java.util.Date
import android.util.Log
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val api: WalletApi
) {
    suspend fun getBalance(token: String): Double {
        Log.d("WalletRepository", "Calling getBalance() with token: $token")
        val response = api.getBalance("Bearer $token")
        Log.d("WalletRepository", "Balance received: ${response.balance}")
        return response.balance
    }

    suspend fun addBalance(token: String, amount: Double): AddBalanceResponse {
        Log.d("WalletRepository", "Calling addBalance() with token: $token and amount: $amount")
        val response = api.addBalance("Bearer $token", amount)

        Log.d("WalletRepository", "AddBalanceResponse received: $response")
        return response
    }

    suspend fun getTransactions(token: String): List<TransactionDto> {
        Log.d("WalletRepository", "Calling getTransactions() with token: $token")
        val response = api.getTransactions("Bearer $token")
        Log.d("WalletRepository", "Transaction response size: ${response.size}")

        val mappedTransactions = response.map {
            val mapped = TransactionDto(
                id = it.id,
                amount = if (it.type == "CREDIT") it.amount else -it.amount,
                type = when (it.type) {
                    "CREDIT" -> TransactionType.CREDIT
                    "DEBIT" -> TransactionType.DEBIT
                    "CASHBACK" -> TransactionType.CASHBACK
                    "REFUND" -> TransactionType.REFUND
                    else -> TransactionType.CREDIT
                }.toString(),
                timestamp = it.timestamp
            )
            Log.d("WalletRepository", "Mapped transaction: $mapped")
            mapped
        }

        return mappedTransactions
    }
}
