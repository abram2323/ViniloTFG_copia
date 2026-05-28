package com.example.vinilotfg.model

data class Direccion(
    val id: String,
    val titulo: String,
    val icono: String,
    val nombre: String,
    val linea1: String,
    val linea2: String,
    val pais: String,
    val predeterminada: Boolean // ¡Este es el campo clave que faltaba!
)