package com.example.vinilotfg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 1. Definimos el degradado aquí arriba para poder usarlo abajo
val LogoGradient = Brush.linearGradient(
    0.3f to Color(0xFFFFFFFF),
    0.6f to Color(0xFFA350F9),
    1.0f to Color(0xFFE9128B),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

// 2. Creamos un estilo personalizado para tu Logo
val LogoTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 32.sp,
    brush = LogoGradient // <--- Aquí aplicamos el degradado
)

// Tu Typography actual se mantiene igual
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
val BackgroundGradient = Brush.linearGradient(
    0.0f to DeepBlueStart,
    1.0f to DeepBlueEnd,
    start = Offset(0f, 0f),                 // Arriba (Top)
    end = Offset(0f, Float.POSITIVE_INFINITY) // Abajo (Bottom)
)