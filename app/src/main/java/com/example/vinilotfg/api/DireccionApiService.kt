package com.example.vinilotfg.api

import com.example.vinilotfg.model.Direccion
import com.example.vinilotfg.model.DireccionRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.PATCH

interface DireccionApiService {
    @GET("api/direcciones/{userId}")
    suspend fun getDirecciones(@Path("userId") userId: String): List<Direccion>

    @POST("api/direcciones")
    suspend fun crearDireccion(@Body direccion: Direccion): Response<String>

    @PATCH("api/direcciones/{id}")
    suspend fun actualizarDireccion(
        @Path("id") id: String,
        @Query("userId") userId: String,
        @Body datos: Map<String, Any>
    ): Response<String>

    @DELETE("api/direcciones/{id}")
    suspend fun eliminarDireccion(
        @Path("id") id: String,
        @Query("userId") userId: String
    ): Response<String>
}