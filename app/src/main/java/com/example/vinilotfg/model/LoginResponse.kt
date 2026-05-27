package com.example.vinilotfg.model

data class LoginResponse(
    val token: String,
    val userId: String,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val socioNum: String,
    val nivel: String,
    val puntosHistorico: Int,
    val puntosDisponibles: Int,
    val error: String? // Por si falla, el servidor devuelve un campo 'error'
)