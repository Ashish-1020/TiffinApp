package com.example.tiffinapp.core.domain

import com.example.tiffinapp.core.data.MealApi
import com.example.tiffinapp.core.data.MealRequest
import com.example.tiffinapp.core.data.MealResponse
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val mealApi: MealApi
) {
    suspend fun getAllMeal(): List<MealResponse> {
        return try {
            val response = mealApi.getMealList()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    suspend fun getMealbyId(mealId:String):MealResponse{
        val response = mealApi.getMealbyId(mealId)
        return response
    }
}
