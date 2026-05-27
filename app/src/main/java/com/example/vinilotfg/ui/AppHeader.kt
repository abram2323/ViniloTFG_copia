package com.example.vinilotfg.ui

import android.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.vinilotfg.ui.theme.*
import androidx.compose.ui.text.font.FontWeight

/**
 * COMPONENTE: AppHeader
 * Esta función define la barra superior (TopAppBar) que se muestra en las diferentes pantallas.
 * * @param title El texto que se mostrará como título en la barra.
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario porque TopAppBar aún se considera una API experimental en Material3
@Composable
fun AppHeader(title: String) {
    TopAppBar(
        // Define el contenido principal de la barra superior
        title = {
            Text(
                text = title,
                color = TextoBlanco,           // Color definido en tu archivo de colores (Color.kt)
                fontWeight = FontWeight.Bold,   // Aplica estilo de fuente en negrita
                style = LogoTextStyle          // Aplica el estilo de texto personalizado definido en tu tema
            )
        },
        // Configura los colores de fondo de la barra
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ColorHeader       // Color de fondo definido en tu tema (ui.theme)
        )
    )
}