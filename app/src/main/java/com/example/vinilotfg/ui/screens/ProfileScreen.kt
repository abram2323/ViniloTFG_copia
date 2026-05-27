package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader

/**
 * Pantalla de perfil del cliente.
 * Muestra la información del usuario, su estado premium y opciones de configuración.
 *
 * @param navController Controlador de navegación para redirigir a otras secciones desde el footer.
 */
@Composable
fun ClientesScreen(navController: NavController) {
    // Paleta de colores coherente con la temática de la app
    val fondoOscuro = Color(0xFF120338)
    val degradadoAvatar = Brush.linearGradient(
        colors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0))
    )

    Scaffold(
        topBar = { AppHeader(title = "Perfil") }, // Cabecera con título fijo
        bottomBar = { AppFooter(navController) }, // Barra de navegación inferior
        containerColor = fondoOscuro
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // SECCIÓN DE AVATAR: Círculo con degradado e icono de persona
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) // Recorta el fondo en forma circular
                    .background(degradadoAvatar),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "Avatar", Modifier.size(60.dp), Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información básica del usuario
            Text("Usuario", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("usuario@gmail.com", color = Color.LightGray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            // ETIQUETA PREMIUM: Uso de Surface para crear un "Badge" o etiqueta estilizada
            Surface(color = Color(0xFF311B92), shape = RoundedCornerShape(20.dp)) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Usuario Premium", color = Color.White, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Spacer(modifier = Modifier.height(25.dp))

            // LISTA DE OPCIONES: Menú vertical de tarjetas interactivas
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OptionRow(Icons.Default.PersonOutline, "Editar perfil")
                OptionRow(Icons.Default.FavoriteBorder, "Mis favoritas")
                OptionRow(Icons.Default.QueueMusic, "Mi carrito")
                // Opción destacada en color dorado
                OptionRow(Icons.Default.WorkspacePremium, "Obtener Premium", Color(0xFFFFD700))
                OptionRow(Icons.Default.Settings, "Configuración")
            }
        }
    }
}

/**
 * Componente genérico para mostrar tarjetas de estadísticas (aunque no se use en el layout actual).
 *
 * @param value El valor numérico o texto principal.
 * @param label La etiqueta descriptiva debajo del valor.
 */
@Composable
fun StatCard(modifier: Modifier, value: String, label: String) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0B4F)),
        border = BorderStroke(1.dp, Color(0xFF311B92))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

/**
 * Componente para representar una fila de opción en el menú de perfil.
 *
 * @param icon Icono a mostrar a la izquierda.
 * @param title Texto descriptivo de la opción.
 * @param iconColor Color del icono (por defecto blanco).
 */
@Composable
fun OptionRow(icon: ImageVector, title: String, iconColor: Color = Color.White) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0B4F)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF311B92))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenedor del icono con fondo más oscuro para contraste
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF120338), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Título de la opción que ocupa el espacio sobrante
            Text(title, color = Color.White, modifier = Modifier.weight(1f), fontSize = 16.sp)
            // Icono de flecha para indicar que es clicable
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}