package com.example.vinilotfg.model

import com.google.gson.annotations.SerializedName

// Asegúrate de que tu modelo se vea así:
data class RegistroRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String
)