package com.example.vinilotfg.api


import com.example.vinilotfg.model.Carrito
import retrofit2.Response
import retrofit2.http.*

interface CarritoApiService {

    @GET("api/carrito")
    suspend fun obtenerCarrito(): Response<List<Carrito>>

    @POST("api/carrito")
    suspend fun agregarProducto(@Body body: Map<String, Any>): Response<Map<String, String>>

    @PATCH("api/carrito/{itemId}")
    suspend fun actualizarCantidad(@Path("itemId") itemId: String, @Body body: Map<String, Int>): Response<Map<String, String>>

    @DELETE("api/carrito/{itemId}")
    suspend fun eliminarItem(@Path("itemId") itemId: String): Response<Map<String, String>>
}