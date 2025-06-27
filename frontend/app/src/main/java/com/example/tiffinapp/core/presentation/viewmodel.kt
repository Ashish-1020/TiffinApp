package com.example.tiffinapp.core.presentation

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
                val response = repository.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null && loginResponse.status) {
                        tokenManager.saveToken(loginResponse.token) // Save JWT token
                        _loginuiState.value = LoginUiState.Success(loginResponse.username)
                    } else {
                        _loginuiState.value = LoginUiState.Error("Invalid credentials")
                    }

                } else {
                    _loginuiState.value = LoginUiState.Error("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _loginuiState.value = LoginUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

}
