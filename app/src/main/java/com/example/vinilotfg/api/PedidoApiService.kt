package com.example.vinilotfg.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET // Para las peticiones de lectura
import retrofit2.http.Path // Para los parámetros en la URL (como el {id})
import com.example.vinilotfg.model.Pedido // IMPORTANTE: Ajusta este paquete si tu clase Pedido está en otra carpeta
import com.example.vinilotfg.model.PedidoRequest

interface PedidoApiService {
    @POST("api/pedidos")
    suspend fun crearPedido(@Body request: PedidoRequest): Response<Map<String, String>>

    @GET("api/pedidos")
    suspend fun obtenerPedidos(): Response<List<Pedido>>

    @GET("api/pedidos/{id}")
    suspend fun obtenerPedido(@Path("id") id: String): Response<Pedido>
}