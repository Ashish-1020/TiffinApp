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
