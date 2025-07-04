package com.example.tiffinapp.core.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MealApi {

    @Multipart
    @POST("/api/meals/upload")
    fun uploadMeal(
        @Part image: MultipartBody.Part,
        @Part("meal") meal: RequestBody
    ): Call<ResponseBody> // Can keep as is for file upload

    @GET("/api/meals/getAll")
    suspend fun getMealList(): Response<List<MealResponse>> // suspend added

    @GET("/api/meals/{mealId}")
    suspend fun getMealbyId(@Path("mealId") mealId: String): MealResponse
}

