package com.example.vinilotfg.api

import com.example.vinilotfg.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApiService {
    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<String>
}