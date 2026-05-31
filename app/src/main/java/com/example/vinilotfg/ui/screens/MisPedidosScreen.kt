package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.model.Pedido
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.example.vinilotfg.viewmodel.VinylViewModel

@Composable
fun MisPedidosScreen(viewModel: VinylViewModel, navController: NavController) {
    val pedidos by viewModel.pedidos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerPedidos()
    }

    // Añadido el mismo degradado de la tienda para que mantenga la estética premium
    val fondoDegradado = Brush.linearGradient(
        colors = listOf(
            Color(0xFF08050F), // fondo (El tono más oscuro para arriba del todo)
            Color(0xFF0C0918), // superficie (El tono intermedio de transición)
            Color(0xFF12103A)  // superficie2 (El azul/morado eléctrico para el fondo de la pantalla)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoDegradado) // Fondo degradado aplicado aquí
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 12.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado elegante entre pedidos
        ) {
            items(pedidos) { pedido ->
                PedidoItem(pedido) {
                    // Opcional: navController.navigate("detalle_pedido/${pedido.id}")
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
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 👇 SUSTITUIDO POR UN ICONO PREMIUM DE PAQUETE/BOLSA QUE SÍ COMPILA AL 100% 👇
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1A3A4D)), // Fondo azul a juego con la app
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Pedido #${pedido.id}",
                    tint = Color.Cyan, // Color cian a juego con los precios
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Pedido #${pedido.id}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                val estadoActual = pedido.estado ?: "Pendiente"
                val colorEstado = if (estadoActual.equals("Entregado", ignoreCase = true)) Color.Cyan else Color.LightGray

                Text(
                    text = "Estado: $estadoActual",
                    color = colorEstado,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${pedido.total ?: 0.0} €",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}