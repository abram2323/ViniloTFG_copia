package com.example.vinilotfg.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Importación limpia

@Composable
fun AppFooter(navController: NavController) { // Usamos NavController directamente
    BottomAppBar(
        modifier = Modifier.fillMaxWidth().height(70.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("store_guest") }, modifier = Modifier.offset(y = 10.dp)) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }

            IconButton(onClick = { /* Buscar */ }, modifier = Modifier.offset(y = 10.dp)) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }

            IconButton(onClick = { navController.navigate("carrito") }, modifier = Modifier.offset(y = 10.dp)) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
            }

            IconButton(
                onClick = { navController.navigate("perfil") },
                modifier = Modifier.offset(y = 10.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
            }
        }
    }
}