package com.example.tiffinapp.core.data

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.UUID
import kotlinx.serialization.Contextual

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


data class WalletBalanceResponse(
    val balance: Double
)

data class AddBalanceResponse(
    val message: String,
    val status: Boolean
)

data class TransactionDto(
    val id: Long,
    val amount: Double,
    val type: String, // CREDIT / DEBIT
    val timestamp: String
)

data class UserDetailDto(
    val userId: Long,
    val phoneNumber: String,
    val address: String
)


@Serializable
data class OrderRequestDto(
    val orderId: String = UUID.randomUUID().toString(),
    val listOfMeals: String,
    val totalCost: Double,
    val transactionType: String,
    val transactionId: String,
    val name: String,
    val address: String,
    val mobileNo: String
)





@Serializable
data class OrderResponseDto(
    val orderId: String,
    val listOfMeals: String,
    @Contextual val totalCost: BigDecimal,
    val transactionType: String,
    val transactionId: String,
    val name: String,
    val address: String,
    val mobileNo: String,
    val deliveryStatus: String,
    val time: String // or use @Contextual if it's LocalDateTime
)


data class  UserDetailFullDto(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,

)

@Serializable
data class OrderStatusMessageDTO(
    val orderId: String,
    val status: String,
    val message: String
)

