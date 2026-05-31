package com.example.vinilotfg.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vinilotfg.model.Producto
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.example.vinilotfg.viewmodel.VinylViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    producto: Producto,
    navController: NavController,
    viewModel: VinylViewModel
) {
    Log.d("DEBUG_DETAIL", "Recibiendo vinilo: ${producto.nombre} | Stock: ${producto.stock} | ID: ${producto.id}")

    // 👇 COMPROBACIÓN RECOBRADA: Miramos si hay perfil; si es null, es invitado 👇
    val usuario by viewModel.usuarioPerfil.collectAsState()
    val esInvitado = (usuario == null)

    val fondoOscuro = Brush.verticalGradient(
        colors = listOf(Color(0xFF071A27), Color(0xFF120338))
    )

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        // 👇 CORREGIDO: Pasamos la bandera al footer para que oculte el carrito y los pedidos 👇
        bottomBar = { AppFooter(navController = navController, isInvitado = esInvitado) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoOscuro)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del Vinilo
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título y Artista
            Text(
                text = producto.nombre ?: "Nombre no disponible",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = producto.artista ?: "Artista no disponible",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Cyan
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila de Precio y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${producto.precio} €",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Surface(
                    color = Color(0xFF311B92),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = producto.estado?.uppercase() ?: "SIN ESTADO",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Descripción
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Descripción del producto",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = producto.descripcion ?: "Este vinilo no dispone de una descripción detallada todavía.",
                    color = Color.LightGray,
                    lineHeight = 22.sp,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ==========================================================
            // 👇 CONTROL DE ACCIONES: BOTÓN DE COMPRA ADAPTADO A INVITADO 👇
            // ==========================================================
            if (esInvitado) {
                // Si es invitado, le mostramos un botón que le redirige al login de forma segura
                Button(
                    onClick = {
                        navController.navigate("inicio") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2A5E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Inicia sesión para comprar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                // Si es un usuario registrado normal, conserva tu lógica de stock original
                Button(
                    onClick = {
                        Log.d("DEBUG_CLICK", "¡Botón pulsado!")
                        viewModel.agregarAlCarrito(producto.id ?: "")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB13CFF)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = producto.stock > 0
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (producto.stock > 0) "Añadir al carrito" else "Sin Stock",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Stock disponible: ${producto.stock} unidades",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}