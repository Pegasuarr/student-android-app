package com.example.frontend.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.local.SessionManager
import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.RegisterRequest
import com.example.frontend.data.network.RetrofitClient
import com.example.frontend.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    var userRole by mutableStateOf<String?>(null)
        private set

    var username by mutableStateOf<String?>(null)
        private set

    init {
        checkSession()
    }

    fun checkSession() {
        val token = sessionManager.fetchAuthToken()
        val role = sessionManager.fetchUserRole()
        val savedUsername = sessionManager.fetchUsername()
        if (token != null) {
            RetrofitClient.authToken = token
            isLoggedIn = true
            userRole = role
            username = savedUsername
        } else {
            isLoggedIn = false
            userRole = null
            username = null
        }
    }

    fun login(usernameInput: String, passwordInput: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.login(LoginRequest(usernameInput, passwordInput))
            result.fold(
                onSuccess = { response ->
                    sessionManager.saveAuthToken(response.token)
                    sessionManager.saveUserRole(response.role)
                    sessionManager.saveUsername(response.username)
                    RetrofitClient.authToken = response.token
                    isLoggedIn = true
                    userRole = response.role
                    username = response.username
                    isLoading = false
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Login failed. Please check credentials."
                    isLoading = false
                }
            )
        }
    }

    fun register(usernameInput: String, emailInput: String, passwordInput: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.register(RegisterRequest(usernameInput, emailInput, passwordInput))
            result.fold(
                onSuccess = { response ->
                    sessionManager.saveAuthToken(response.token)
                    sessionManager.saveUserRole(response.role)
                    sessionManager.saveUsername(response.username)
                    RetrofitClient.authToken = response.token
                    isLoggedIn = true
                    userRole = response.role
                    username = response.username
                    isLoading = false
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Registration failed."
                    isLoading = false
                }
            )
        }
    }

    fun logout(onSuccess: () -> Unit) {
        sessionManager.clearSession()
        RetrofitClient.authToken = null
        isLoggedIn = false
        userRole = null
        username = null
        onSuccess()
    }

    fun clearError() {
        errorMessage = null
    }
}
