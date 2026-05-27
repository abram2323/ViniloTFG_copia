package com.example.vinilotfg.api

import com.example.vinilotfg.model.Producto
import retrofit2.http.GET

interface ProductoApiService {
    @GET("api/productos") // Esta es la ruta Backend (Spring Boot) expone
    suspend fun getProductos(): List<Producto> // Aquí usamos tu modelo 'Vinyl'
}