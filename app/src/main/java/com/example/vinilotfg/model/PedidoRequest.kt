package com.example.vinilotfg.model

data class PedidoRequest(
    val total: Double,
    val subtotal: Double,
    val envio: Double,
    val descuento: Double,
    val metodoEnvio: String,
    val direccion: String,
    val tarjetaLast4: String,
    val items: String // Él espera que esto sea un JSON String
)