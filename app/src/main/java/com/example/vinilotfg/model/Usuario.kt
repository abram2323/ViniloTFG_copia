package com.example.vinilotfg.model

/**
 * Modelo de Usuario adaptado a Kotlin
 * Representa los datos del perfil en la tabla vs_usuarios de Supabase
 */
data class Usuario(
    var id: String? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var email: String? = null,
    var telefono: String? = null,
    var foto: String? = null,
    var fotoUrl: String? = null,
    var socioNum: String? = null,
    var nivel: String? = null,
    var puntosHistorico: Int = 0,
    var puntosDisponibles: Int = 0,
    var createdAt: String? = null
)