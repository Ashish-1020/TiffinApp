package com.example.tiffinapp.core.presentation

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiffinapp.core.data.MealApi
import com.example.tiffinapp.core.data.MealRequest
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

sealed class UploadUiState {
    object Idle : UploadUiState()
    object Loading : UploadUiState()
    data class Success(val message: String) :UploadUiState()
    data class Error(val message: String) :UploadUiState()
}

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealApi: MealApi,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uploaduiState= MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uploaduiState: StateFlow<UploadUiState> = _uploaduiState

    fun uploadMeal(imageUri: Uri, meal: MealRequest) {
        _uploaduiState.value=UploadUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes() ?: return@launch

                val imagePart = MultipartBody.Part.createFormData(
                    "image", "meal.jpg",
                    bytes.toRequestBody("image/*".toMediaTypeOrNull())
                )
                val mealJson = Gson().toJson(meal)
                val mealPart = mealJson.toRequestBody("application/json".toMediaTypeOrNull())

                val response = mealApi.uploadMeal(imagePart, mealPart).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(context, "Meal uploaded successfully", Toast.LENGTH_LONG).show()
                        _uploaduiState.value=UploadUiState.Success(body.toString())
                    } else {
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show()
                        _uploaduiState.value=UploadUiState.Error(response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    _uploaduiState.value=UploadUiState.Error(e.message.toString())
                }
            }
        }
    }
}
