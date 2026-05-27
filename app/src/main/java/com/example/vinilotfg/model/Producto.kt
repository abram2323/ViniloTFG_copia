package com.example.vinilotfg.model

/**
 * Modelo de Producto adaptado a Kotlin
 * Representa un producto de la tabla 'productos' de Supabase
 */
data class Producto(
    var id: String? = null,
    var nombre: String? = null,
    var descripcion: String? = null,
    var genero: String? = null,
    var precio: Double = 0.0,
    var stock: Int = 0,
    var imagenUrl: String? = null,
    var estado: String? = null,
    var artista: String? = null,
    var sku: String? = null,
    var categoria: String? = null,
    var formato: String? = null,
    var anio: Int? = null, // Usamos Int? para permitir nulos igual que Integer
    var createdAt: String? = null
)