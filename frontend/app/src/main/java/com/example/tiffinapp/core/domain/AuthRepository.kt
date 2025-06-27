package com.example.tiffinapp.core.domain
// repository/AuthRepository.kt



import com.example.tiffinapp.core.data.AuthApi
import com.example.tiffinapp.core.data.LoginRequest
import com.example.tiffinapp.core.data.LoginResponse
import com.example.tiffinapp.core.data.SignupRequest
import com.example.tiffinapp.core.data.SignupResponse
import com.example.tiffinapp.core.util.TokenManager
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {

    suspend fun signup(request: SignupRequest): Response<SignupResponse> {
        return api.signup(request)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return api.login(request)
    }


    suspend fun validateJwt(request: String): Response<Boolean> {
        val response = api.validateJwt("Bearer $request")
        return api.validateJwt(request)
    }
}

