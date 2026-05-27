package com.example.vinilotfg.model

/**
 * Modelo de Carrito adaptado a Kotlin
 * Representa un ítem en la tabla 'vs_carrito' de Supabase
 */
data class Carrito(
    var id: String? = null,
    var usuarioId: String? = null,
    var productoId: String? = null,
    var cantidad: Int = 0,
    var createdAt: String? = null,

    /* Datos del producto — se rellenan al hacer JOIN */
    var nombre: String? = null,
    var artista: String? = null,
    var precio: Double = 0.0,
    var imagenUrl: String? = null,
    var genero: String? = null,
    var stock: Int = 0,
    var categoria: String? = null
)