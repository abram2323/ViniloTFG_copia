package com.example.vinilotfg.model

/**
 * Modelo de Pedido adaptado a Kotlin
 * Representa un pedido en la tabla 'vs_pedidos' de Supabase
 */
data class Pedido(
    var id: String? = null,
    var usuarioId: String? = null,
    var estado: String? = null,
    var total: Double = 0.0,
    var subtotal: Double = 0.0,
    var envio: Double = 0.0,
    var descuento: Double = 0.0,
    var metodoEnvio: String? = null,
    var direccion: String? = null,
    var tarjetaLast4: String? = null,
    var items: String? = null, // Podrías cambiar esto a un objeto/lista si lo prefieres
    var createdAt: String? = null
)