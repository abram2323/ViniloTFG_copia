package com.example.vinilotfg.api

import com.example.vinilotfg.model.Direccion
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface DireccionApiService {

    // Cambia esto
    @GET("/api/direcciones/{userId}")
    suspend fun obtenerDirecciones(@Path("userId") userId: String): Response<List<Direccion>>

    @POST("/api/direcciones")
    suspend fun crearDireccion(@Body direccion: Direccion): Response<String>

    @DELETE("/api/direcciones/{id}")
    suspend fun eliminarDireccion(@Path("id") id: String, @Query("userId") userId: String): Response<String>
}