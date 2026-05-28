package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.model.Pedido
import com.example.vinilotfg.viewmodel.VinylViewModel
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text // Asegúrate de usar material3
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader


@Composable
fun MisPedidosScreen(viewModel: VinylViewModel, navController: NavController) {
    // Observamos el estado de los pedidos que definimos antes
    val pedidos by viewModel.pedidos.collectAsState()

    // Carga los pedidos automáticamente al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerPedidos()
    }

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(pedidos) { pedido ->
                PedidoItem(pedido) {
                    // Opcional: Navegar al detalle del pedido si tu compañero tiene esa ruta
                    // navController.navigate("detalle_pedido/${pedido.id}")
                }
            }
        }
    }
}

@Composable
fun PedidoItem(pedido: Pedido, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // <--- ASÍ SE HACE AHORA
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Pedido #${pedido.id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Total: ${pedido.total}€")
            Text(text = "Estado: ${pedido.estado ?: "Pendiente"}") // Ajusta según tu modelo
        }
    }
}