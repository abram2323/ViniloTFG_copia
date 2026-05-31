package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.model.Carrito
import com.example.vinilotfg.viewmodel.VinylViewModel
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background

@Composable
fun CartScreen(viewModel: VinylViewModel, navController: NavController) {
    // 1. Estado y cálculos
    val cartItems by viewModel.carritoItems.collectAsState()
    val total = cartItems.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }

    val fondoDegradado = Brush.linearGradient(
        colors = listOf(
            Color(0xFF08050F), // fondo (El tono más oscuro para arriba del todo)
            Color(0xFF0C0918), // superficie (El tono intermedio de transición)
            Color(0xFF12103A)  // superficie2 (El azul/morado eléctrico para el fondo de la pantalla)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    // 2. Carga inicial
    LaunchedEffect(Unit) {
        viewModel.obtenerCarrito()
    }

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) },
        containerColor = Color.Transparent // 👈 1. Ponemos transparente aquí para que no tape el degradado
    ) { padding ->

        // 👈 2. Envolvemos TODO en un Box con el fondoDegradado para que ocupe toda la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoDegradado)
                .padding(padding)
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío.", color = Color.White)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Lista de productos
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 12.dp) // Espaciado superior e inferior en la lista
                    ) {
                        items(cartItems) { item ->
                            CartItemRow(item = item, viewModel = viewModel)
                        }
                    }

                    // Resumen del pedido y botón (Sustituido Surface por una Card para mantener tu diseño premium)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), // Curva elegante solo arriba
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0918)), // Mismo color 'superficie'
                        elevation = CardDefaults.cardElevation(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Total:",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${String.format("%.2f", total)} €",
                                    style = MaterialTheme.typography.headlineMedium, // Un poco más grande el total
                                    color = Color.Cyan, // Precio en cian brillante
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { navController.navigate("payment_screen") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp), // Volvemos exactamente a los 50.dp que tenías originalmente
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(0.dp), // Elimina márgenes internos por defecto de Material3
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,     // Fondo base transparente
                                    disabledContainerColor = Color.Transparent // Fondo deshabilitado transparente
                                ),
                                elevation = null // Quitamos cualquier sombra por defecto que ensucie el borde del botón
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize() // Fuerza al degradado a expandirse de extremo a extremo del botón
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFFF006E), // grad_fuego_inicio (#FF006E)
                                                    Color(0xFFFF6B00)  // grad_fuego_fin (#FF6B00)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Finalizar pedido",
                                        fontWeight = FontWeight.Bold, // Volvemos a tu tipografía en negrita estándar
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge // Restauramos tu estilo de tamaño original
                                    )
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
fun CartItemRow(item: Carrito, viewModel: VinylViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp), // Alineado con los márgenes de la tienda
        colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)), // Mismo color oscuro premium
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp) // Mismos bordes redondeados
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Imagen izquierda estilizada (Igual que ProductoItem)
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = "Imagen de ${item.nombre}",
                modifier = Modifier
                    .size(90.dp) // Un pelín más grande para equilibrar los botones
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Bloque de información y controles
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título del Vinilo
                Text(
                    text = item.nombre ?: "Producto",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                // Artista (¡Añadido para igualar el diseño!)
                Text(
                    text = item.artista ?: "Desconocido",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila inferior: Precio a la izquierda, Controles de cantidad a la derecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Precio destacado en cian
                    Text(
                        text = "${item.precio ?: 0.0} €",
                        color = Color.Cyan,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    // Controles de cantidad compactos y limpios
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Botón Menos
                        FilledIconButton(
                            onClick = { viewModel.actualizarCantidad(item.id, (item.cantidad ?: 1) - 1) },
                            modifier = Modifier.size(32.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = Color(0xFF1A3A4D), // Tono azul del degradado para los botones
                                contentColor = Color.White
                            )
                        ) {
                            Text("-", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        }

                        // Cantidad numérica
                        Text(
                            text = "${item.cantidad ?: 1}",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        // Botón Más
                        FilledIconButton(
                            onClick = { viewModel.actualizarCantidad(item.id, (item.cantidad ?: 1) + 1) },
                            modifier = Modifier.size(32.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = Color(0xFF1A3A4D),
                                contentColor = Color.White
                            )
                        ) {
                            Text("+", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón Eliminar (Papelera) en color rojo de error
                        IconButton(
                            onClick = { viewModel.eliminarProducto(item.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar producto",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}