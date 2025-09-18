package com.example.tiffinapp.core.presentation

import android.util.Log
import com.example.tiffinapp.core.data.SignupRequest
import com.example.tiffinapp.core.domain.AuthRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiffinapp.core.data.LoginRequest
import com.example.tiffinapp.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val message: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: String) :LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _loginuiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginuiState: StateFlow<LoginUiState> = _loginuiState
    fun registerUser(fullName: String, email: String, password: String) {
        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.signup(
                    SignupRequest(fullName, email, password)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.message.trim() == "User registered successfully.") {
                        _uiState.value = RegisterUiState.Success(body.message)
                    } else {
                        _uiState.value = RegisterUiState.Error("Unexpected response: ${body?.message ?: "null"}")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    _uiState.value = RegisterUiState.Error("Signup failed: ${response.code()} | $error")
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Unknown error")
            }
        }
    }



    fun loginUser(email: String, password: String) {
        _loginuiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(email.trim(), password.trim()))

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.status == true && !body.token.isNullOrBlank()) {
                        // Save token and user ID
                        tokenManager.saveToken(body.token)
                        tokenManager.saveUserId(body.id.toLong())

                        _loginuiState.value = LoginUiState.Success(body.username)
                    } else {
                        _loginuiState.value = LoginUiState.Error(
                            body?.message ?: "Invalid email or password"
                        )
                    }

                } else {
                    // Handle specific HTTP errors if needed
                    val message = when (response.code()) {
                        401 -> "Unauthorized: Invalid credentials"
                        403 -> "Access denied"
                        500 -> "Server error. Please try again later."
                        else -> "Login failed with code ${response.code()}"
                    }
                    _loginuiState.value = LoginUiState.Error(message)
                }

            } catch (e: Exception) {
                val message = when (e) {
                    is IOException -> "Network error. Check your internet connection."
                    is HttpException -> "Unexpected server response. Please try again."
                    else -> "Something went wrong. Please try again later."
                }

                // Log full error for debugging, but don't show it to users
                Log.e("LoginViewModel", "Login error", e)
                _loginuiState.value = LoginUiState.Error(message)
            }
        }
    }


}
