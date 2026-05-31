package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.example.vinilotfg.viewmodel.VinylViewModel

@Composable
fun ClientesScreen(navController: NavController, viewModel: VinylViewModel) {
    val usuario by viewModel.usuarioPerfil.collectAsState()

    // 👈 DETERMINAMOS SI ES INVITADO: Si el perfil en el ViewModel es null, es invitado
    val esInvitado = (usuario == null)

    val fondoDegradado = Brush.linearGradient(
        colors = listOf(Color(0xFF08050F), Color(0xFF0C0918), Color(0xFF12103A)),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    val degradadoAvatar = Brush.linearGradient(
        colors = listOf(Color(0xFFFF006E), Color(0xFF7B2FFF))
    )

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        // Pasamos la validación al footer para que mantenga oculto el carrito/pedidos
        bottomBar = { AppFooter(navController = navController, isInvitado = esInvitado) },
        containerColor = Color.Transparent
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoDegradado)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                if (esInvitado) {
                    // ==========================================
                    // SHIELD UI: VISTA EXCLUSIVA PARA INVITADOS
                    // ==========================================
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0x1AFFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, "Invitado", Modifier.size(60.dp), Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Modo Invitado",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Inicia sesión o crea una cuenta para disfrutar de todo nuestro catálogo, añadir vinilos al carrito y gestionar tus pedidos personalizados.",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón con tu degradado de fuego exacto sin bordes raros
                    Button(
                        onClick = {
                            navController.navigate("inicio") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFF006E), Color(0xFFFF6B00))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Iniciar Sesión / Registrarse",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                } else {
                    // ==========================================
                    // VISTA NORMAL: USUARIO LOGEADO CON ÉXITO
                    // ==========================================
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

                    Text(
                        text = usuario?.nombre ?: "Usuario",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = usuario?.email ?: "",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OptionRow(Icons.Outlined.Person, "Editar perfil")
                        OptionRow(Icons.Default.QueueMusic, "Devoluciones")
                        OptionRow(Icons.Default.WorkspacePremium, "Direcciones") {
                            navController.navigate("direcciones")
                        }

                        OptionRow(Icons.Default.Settings, "Cerrar sesión", iconColor = Color(0xFFFF006E)) {
                            viewModel.cerrarSesion { exito ->
                                if (exito) {
                                    navController.navigate("inicio") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
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
                    .background(Color(0xFF0C0918), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}