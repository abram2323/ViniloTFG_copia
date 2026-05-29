package com.example.vinilotfg.model

data class DireccionRequest(
    val userId: String,
    val titulo: String,
    val icono: String,
    val nombre: String,
    val linea1: String,
    val linea2: String?,
    val pais: String,
    val predeterminada: Boolean = false
)