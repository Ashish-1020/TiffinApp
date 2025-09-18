package com.example.tiffinapp.core.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserDetailApi {

    @GET("/api/user-details")
    suspend fun getDetail(@Header("Authorization") token: String): UserDetailDto

    @GET("/api/user-details/full")
    suspend fun getfullDetail(@Header("Authorization") token: String): UserDetailFullDto

    @POST("/api/user-details")
    suspend fun createDetail(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): UserDetailDto

    @PUT("/api/user-details")
    suspend fun updateDetail(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): UserDetailDto

    @DELETE("/api/user-details")
    suspend fun deleteDetail(@Header("Authorization") token: String)
}
