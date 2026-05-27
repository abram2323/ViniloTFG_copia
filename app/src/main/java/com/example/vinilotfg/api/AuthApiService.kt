package com.example.vinilotfg.api

import com.example.vinilotfg.model.LoginRequest
import com.example.vinilotfg.model.LoginResponse
import com.example.vinilotfg.model.RegistroRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/register")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<Map<String, String>>

    @POST("/api/auth/login") // La ruta que tu compañero tenga para el login
    suspend fun loginUsuario(@Body request: LoginRequest): Response<LoginResponse>
}