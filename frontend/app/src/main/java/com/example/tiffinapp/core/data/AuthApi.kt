package com.example.tiffinapp.core.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/register")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("/login")
    suspend fun  login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/validateJwt")
    suspend fun validateJwt(
        @Header("Authorization") token: String
    ): Response<Boolean>

}