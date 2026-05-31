package com.example.vinilotfg.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vinilotfg.model.Producto
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.example.vinilotfg.viewmodel.VinylViewModel
import kotlinx.coroutines.delay

@Composable
fun StoreScreen(
    username: String?,
    navController: NavController,
    viewModel: VinylViewModel
) {
    val productoList by viewModel.vinyls.collectAsState()
    // 👇 RECUPERADO: Observamos el perfil del usuario desde el ViewModel 👇
    val usuario by viewModel.usuarioPerfil.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isGrid by remember { mutableStateOf(false) }

    var currentPage by remember { mutableIntStateOf(1) }
    val itemsPerPage = 10

    val esModoInvitado = (username == null)

// 👇 CORREGIDO: Solo pedimos el perfil si NO somos un invitado y el estado está vacío 👇
    LaunchedEffect(Unit) {
        if (!esModoInvitado && usuario == null) {
            viewModel.obtenerPerfil()
        }
    }

    // 👇 RECUPERADO: Forzamos la carga del perfil dinámico al entrar a la pantalla 👇
    LaunchedEffect(Unit) {
        if (usuario == null) {
            viewModel.obtenerPerfil()
        }
    }

    val filteredProductos = productoList.filter {
        (it.nombre ?: "").contains(searchQuery, ignoreCase = true) ||
                (it.artista ?: "").contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(searchQuery) {
        currentPage = 1
    }

    val totalItems = filteredProductos.size
    val maxPage = if (totalItems == 0) 1 else kotlin.math.ceil(totalItems.toDouble() / itemsPerPage).toInt()

    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = minOf(startIndex + itemsPerPage, totalItems)
    val paginatedProductos = if (startIndex < totalItems) {
        filteredProductos.subList(startIndex, endIndex).toList()
    } else {
        emptyList()
    }

    val fondoDegradado = Brush.linearGradient(
        colors = listOf(
            Color(0xFF08050F),
            Color(0xFF0C0918),
            Color(0xFF12103A)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController = navController, isInvitado = esModoInvitado) }
    ) { paddingValues ->

        if (productoList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF071A27)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fondoDegradado)
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (searchQuery.isEmpty()) {
                    item {
                        FeaturedCarousel(productos = productoList.take(10), navController = navController)
                    }
                }

                // 👇 RECUPERADO Y MEJORADO: Texto de bienvenida dinámico si el usuario está logeado 👇
                if (usuario != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Bienvenido, ${usuario?.nombre}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        placeholder = { Text("Buscar productos...", color = Color.LightGray) },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = { isGrid = !isGrid }) {
                                Icon(
                                    imageVector = if (isGrid) Icons.AutoMirrored.Filled.List else Icons.Filled.GridView,
                                    contentDescription = "Cambiar vista",
                                    tint = Color.LightGray
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.Cyan
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { if (currentPage > 1) currentPage-- },
                            enabled = currentPage > 1,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Cyan, disabledContentColor = Color.Gray)
                        ) {
                            Text("Anterior", fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "Página $currentPage de $maxPage",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        TextButton(
                            onClick = { if (currentPage < maxPage) currentPage++ },
                            enabled = currentPage < maxPage,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Cyan, disabledContentColor = Color.Gray)
                        ) {
                            Text("Siguiente", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isGrid) {
                    items(paginatedProductos.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { prod ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductoItem(prod, true, navController)
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    items(paginatedProductos) { prod ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            ProductoItem(prod, false, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, isGrid: Boolean, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detalle/${producto.id ?: ""}") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (isGrid) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = producto.imagenUrl,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = producto.nombre ?: "Sin nombre",
                        color = Color.White,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto.artista ?: "Desconocido",
                        color = Color.LightGray,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${producto.precio ?: 0.0} €",
                        color = Color.Cyan,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = producto.imagenUrl,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(85.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre ?: "Sin nombre",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = producto.artista ?: "Desconocido",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${producto.precio ?: 0.0} €",
                        color = Color.Cyan,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedCarousel(
    productos: List<Producto>,
    navController: NavController
) {
    if (productos.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % productos.size
        }
    }

    val currentProducto = productos[currentIndex]

    Box(modifier = Modifier.fillMaxWidth()) {
        Crossfade(
            targetState = currentProducto,
            animationSpec = tween(durationMillis = 1000),
            label = "DesvanecimientoProducto"
        ) { productoToShow ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .clickable {
                        navController.navigate("detalle/${productoToShow.id ?: ""}")
                    },
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                AsyncImage(
                    model = productoToShow.imagenUrl,
                    contentDescription = productoToShow.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}