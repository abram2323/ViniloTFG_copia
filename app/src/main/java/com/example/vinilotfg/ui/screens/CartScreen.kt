package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.model.Carrito
import com.example.vinilotfg.viewmodel.VinylViewModel

@Composable
fun CartScreen(viewModel: VinylViewModel, navController: NavController) {
    // Observamos los cambios en el carrito
    val cartItems by viewModel.carritoItems.collectAsState()

    // Cargar los items al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerCarrito()
    }

    Scaffold(
                topBar = { TopAppBar(title = { Text("Mi Carrito") }) }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(cartItems) { item ->
                    CartItemRow(item)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: Carrito){
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre ?: "Producto desconocido", style = MaterialTheme.typography.titleMedium)
                Text(text = "Cantidad: ${item.cantidad}")
                Text(text = "Precio: ${item.precio}€")
            }
        }
    }
}