package com.example.vinilotfg.api

import com.example.vinilotfg.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {

    // 1. Crear nuevo perfil
    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<String>

    // 2. Obtener perfil por ID
    @GET("api/usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id: String): Response<Usuario>

    @GET("api/auth/me")
    suspend fun obtenerMiPerfil(): Response<Map<String, Any>>

    // 3. Actualizar perfil
    @PATCH("api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: String,
        @Body datos: Map<String, Any>
    ): Response<String>
}