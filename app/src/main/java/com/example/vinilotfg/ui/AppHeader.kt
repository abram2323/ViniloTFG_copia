package com.example.vinilotfg.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.vinilotfg.ui.theme.*

/**
 * COMPONENTE: AppHeader
 * Esta función define la barra superior (TopAppBar) que se muestra en las diferentes pantallas.
 * @param title El texto que se mostrará como título en la barra.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String) {
    TopAppBar(
        title = {
            // 1. Definimos el degradado lineal con tus 4 colores hexadecimales
            val degradadoLogo = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF0EBFF), // grad_logo_1 (Blanco/Púrpura suave)
                    Color(0xFF7B2FFF), // grad_logo_2 (Morado)
                    Color(0xFFFF006E), // grad_logo_3 (Rosa eléctrico)
                    Color(0xFFFF6B00)  // grad_logo_4 (Naranja)
                )
            )

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                // 2. Modificamos el estilo existente para inyectarle el degradado (Brush)
                style = LogoTextStyle.copy(
                    brush = degradadoLogo
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // 👇 AQUÍ HEMOS PUESTO TU NUEVO COLOR CON EL FORMATO DE COMPOSE (0xF708050F) 👇
            containerColor = Color(0xF708050F)
        )
    )
}