package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.vinilotfg.viewmodel.VinylViewModel

/**
 * Pantalla de perfil del cliente.
 * Muestra la información del usuario, su estado premium y opciones de configuración.
 */
@Composable
fun ClientesScreen(navController: NavController, viewModel: VinylViewModel) {
    // Observamos los datos reales del usuario desde el ViewModel
    val usuario by viewModel.usuarioPerfil.collectAsState()

    val fondoOscuro = Color(0xFF120338)
    val degradadoAvatar = Brush.linearGradient(
        colors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0))
    )

    // Carga inicial del perfil
    LaunchedEffect(Unit) {
        val userId = viewModel.currentUserId
        if (userId != null) {
            viewModel.obtenerPerfil()
        }
    }

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) },
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

            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(degradadoAvatar),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "Avatar", Modifier.size(60.dp), Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información dinámica
            Text(
                usuario?.nombre ?: "Cargando...",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                usuario?.email ?: "",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Opciones de configuración
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OptionRow(Icons.Default.PersonOutline, "Editar perfil")
                OptionRow(Icons.Default.QueueMusic, "Devoluciones")
                OptionRow(Icons.Default.WorkspacePremium, "Direcciones") {
                    navController.navigate("direcciones")
                }

                // Cerrar sesión
                OptionRow(Icons.Default.Settings, "Cerrar sesión") {
                    viewModel.cerrarSesion {
                        navController.navigate("inicio") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionRow(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0B4F)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF311B92))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF120338), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                title,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp
            )
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}