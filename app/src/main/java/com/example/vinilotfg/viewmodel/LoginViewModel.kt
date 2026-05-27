package com.example.vinilotfg.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    // Estado del login
    var loginStatus by mutableStateOf<LoginStatus>(LoginStatus.Idle)
        private set

    fun performLogin(email: String, password: String) {
        // AQUÍ es donde, en el futuro, se hará la llamada a Spring Boot con Retrofit
        // Por ahora, validamos localmente para que tu app siga funcionando
        if (email.isNotBlank() && password.isNotBlank()) {
            loginStatus = LoginStatus.Success(email)
        } else {
            loginStatus = LoginStatus.Error("Credenciales vacías")
        }
    }
}

sealed class LoginStatus {
    object Idle : LoginStatus()
    data class Success(val email: String) : LoginStatus()
    data class Error(val message: String) : LoginStatus()
}