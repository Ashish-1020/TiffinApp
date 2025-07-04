package com.example.tiffinapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiffinapp.core.data.MealResponse
import com.example.tiffinapp.core.domain.MealRepository
import com.example.tiffinapp.core.presentation.RegisterUiState
import com.example.tiffinapp.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository,private val tokenManager: TokenManager
) : ViewModel() {

    private val _mealList = MutableStateFlow<List<MealResponse>>(emptyList())
    val mealList: StateFlow<List<MealResponse>> = _mealList
    private val _meal= MutableStateFlow<MealResponse>(MealResponse("","","",0.0,0,0,0,"","","",
        emptyList()
    ))
    val meal: StateFlow<MealResponse> = _meal

    fun getAllMeal() {
        viewModelScope.launch {
            try {
                val response = mealRepository.getAllMeal()
                if (response.isNotEmpty()) {
                    _mealList.value = response
                }
            } catch (e: Exception) {
                // Handle/log error (optional)
                e.printStackTrace()
            }
        }
    }


    fun getMealbyId(mealId: String) {
        viewModelScope.launch {
            try {
                val response = mealRepository.getMealbyId(mealId)

                    _meal.value = response

            } catch (e: Exception) {
                // Handle/log error (optional)
                e.printStackTrace()
            }
        }
    }



}
