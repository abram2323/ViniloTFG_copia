package com.example.vinilotfg.api


import com.example.vinilotfg.model.Carrito
import com.example.vinilotfg.model.CarritoRequest
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.POST
interface CarritoApiService {

    @GET("api/carrito")
    suspend fun obtenerCarrito(): Response<List<Carrito>>

    // Usa Map<String, Any> explícitamente para que Retrofit sepa qué es
    @POST("api/carrito")
    suspend fun agregarProducto(@Body request: CarritoRequest): retrofit2.Response<Unit>

    @PATCH("api/carrito/{itemId}")
    suspend fun actualizarCantidad(
        @Path("itemId") itemId: String,
        @Body body: Map<String, Int> // Cambiado de 'Any' a 'Int'
    ): Response<Map<String, String>>

    @DELETE("api/carrito/{itemId}")
    suspend fun eliminarItem(@Path("itemId") itemId: String): Response<Map<String, String>>
}