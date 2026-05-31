package com.example.vinilotfg.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppFooter(navController: NavController, isInvitado: Boolean = false) { // 👈 Añadida bandera de control
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth().height(70.dp),
        containerColor = Color(0xF708050F),
        tonalElevation = 0.dp
    ) {
        // 1. Botón HOME (Tienda) - Se ilumina con cualquiera de las dos variantes de la tienda
        val esPantallaTienda = currentRoute == "store_guest" || currentRoute?.startsWith("store/") == true

        NavigationBarItem(
            selected = esPantallaTienda,
            onClick = {
                // Si ya está en la tienda, evitamos re-navegar innecesariamente
                if (!esPantallaTienda) {
                    if (isInvitado) navController.navigate("store_guest") else navController.navigate("store/false")
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color(0x8CFFFFFF),
                indicatorColor = Color(0x33FFFFFF)
            )
        )

        // 👇 FILTRO DE SEGURIDAD VISUAL: Ocultamos pedidos y carrito si es invitado 👇
        if (!isInvitado) {
            // 2. Botón MIS PEDIDOS
            NavigationBarItem(
                selected = currentRoute == "mis_pedidos",
                onClick = { navController.navigate("mis_pedidos") },
                icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Mis pedidos") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0x8CFFFFFF),
                    indicatorColor = Color(0x33FFFFFF)
                )
            )

            // 3. Botón CARRITO
            NavigationBarItem(
                selected = currentRoute == "carrito",
                onClick = { navController.navigate("carrito") },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0x8CFFFFFF),
                    indicatorColor = Color(0x33FFFFFF)
                )
            )
        }

        // 4. Botón PERFIL (Siempre visible)
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = { navController.navigate("perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color(0x8CFFFFFF),
                indicatorColor = Color(0x33FFFFFF)
            )
        )
    }
}