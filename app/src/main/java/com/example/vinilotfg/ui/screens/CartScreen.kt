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

@Composable
fun CartScreen(viewModel: VinylViewModel, navController: NavController) {
    // 1. Estado y cálculos
    val cartItems by viewModel.carritoItems.collectAsState()
    val total = cartItems.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }
    val fondoOscuro = Color(0xFF120338)

    // 2. Carga inicial
    LaunchedEffect(Unit) {
        viewModel.obtenerCarrito()
    }

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) },
        containerColor = fondoOscuro
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío.", color = Color.White)
            }
        } else {
            Column(modifier = Modifier.padding(padding)) {
                // Lista de productos
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemRow(item = item, viewModel = viewModel)
                    }
                }

                // Resumen del pedido y botón
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "${String.format("%.2f", total)}€",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("payment_screen") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Finalizar pedido")
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
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = "Imagen de ${item.nombre}",
                modifier = Modifier.size(80.dp).padding(end = 16.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            // Detalles y botones
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre ?: "Producto", style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: ${item.precio}€")

                // Controles de cantidad
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        viewModel.actualizarCantidad(item.id, (item.cantidad ?: 1) - 1) }) {
                        Text("-")
                    }

                    Text(
                        text = "${item.cantidad}",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(onClick = { viewModel.actualizarCantidad(item.id, (item.cantidad ?: 1) + 1) }) {
                        Text("+")
                    }
                }

                IconButton(onClick = { viewModel.eliminarProducto(item.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar producto",
                        tint = MaterialTheme.colorScheme.error // Rojo para indicar acción de borrar
                    )
                }
            }

        }
    }
}