package com.example.frontend.data.repository

import com.example.frontend.data.model.AuthResponse
import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.RegisterRequest
import com.example.frontend.data.network.RetrofitClient

class AuthRepository {

    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // Set the token on login
                RetrofitClient.authToken = body.token
                Result.success(body)
            } else {
                val error = response.errorBody()?.string() ?: "Login failed"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.apiService.register(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // Set the token on register
                RetrofitClient.authToken = body.token
                Result.success(body)
            } else {
                val error = response.errorBody()?.string() ?: "Registration failed"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
