package com.example.vinilotfg.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.viewmodel.VinylViewModel
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun PaymentScreen(navController: NavController, viewModel: VinylViewModel) {
    var pasoActual by remember { mutableIntStateOf(1) }

    var direccion by remember { mutableStateOf("") }
    var metodoEnvio by remember { mutableStateOf("Estándar") }
    var tarjetaLast4 by remember { mutableStateOf("4242") }

    val usuarioActual by viewModel.usuarioPerfil.collectAsState()
    val context = LocalContext.current
    val items by viewModel.carritoItems.collectAsState()
    val total = items.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }

    // El degradado de fondo oficial de toda la app
    val fondoDegradado = Brush.linearGradient(
        colors = listOf(Color(0xFF08050F), Color(0xFF0C0918), Color(0xFF12103A)),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        // Pasamos si es invitado calculándolo dinámicamente para proteger el menú inferior
        bottomBar = { AppFooter(navController = navController, isInvitado = (usuarioActual == null)) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoDegradado)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // Indicador visual superior del paso en el que se encuentra el usuario
                PasosCheckoutHeader(pasoActual = pasoActual)
                Spacer(modifier = Modifier.height(16.dp))

                when (pasoActual) {
                    1 -> FormularioEnvio(
                        onSiguiente = { nuevaDir -> direccion = nuevaDir; pasoActual = 2 }
                    )
                    2 -> FormularioPago(
                        onSiguiente = { nuevaTarjeta ->
                            tarjetaLast4 = nuevaTarjeta
                            pasoActual = 3
                        },
                        onAtras = { pasoActual = 1 }
                    )
                    3 -> ResumenConfirmacion(
                        navController = navController,
                        viewModel = viewModel,
                        metodoEnvio = metodoEnvio,
                        direccion = direccion,
                        tarjeta = tarjetaLast4,
                        onConfirmar = {
                            viewModel.crearPedido(
                                total = total,
                                subtotal = total,
                                envio = 0.0,
                                descuento = 0.0,
                                metodoEnvio = metodoEnvio,
                                direccion = direccion,
                                tarjetaLast4 = tarjetaLast4.takeLast(4),
                                listaCarrito = items,
                                onSuccess = {
                                    viewModel.limpiarCarritoEnServidor()
                                    val nombre = usuarioActual?.nombre
                                    if (!nombre.isNullOrEmpty() && nombre != "Sin nombre") {
                                        Toast.makeText(context, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
                                        navController.navigate("store/$nombre") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("store_guest") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PasosCheckoutHeader(pasoActual: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val activoColor = Color.Cyan
        val inactivoColor = Color.Gray.copy(alpha = 0.5f)

        Text("1. Envío", color = if (pasoActual >= 1) activoColor else inactivoColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("—", color = inactivoColor)
        Text("2. Pago", color = if (pasoActual >= 2) activoColor else inactivoColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("—", color = inactivoColor)
        Text("3. Revisión", color = if (pasoActual == 3) activoColor else inactivoColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun FormularioEnvio(onSiguiente: (String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var metodoEnvio by remember { mutableStateOf("Estándar") }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.Cyan,
        unfocusedBorderColor = Color(0x33FFFFFF),
        focusedLabelColor = Color.Cyan,
        unfocusedLabelColor = Color.Gray
    )

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Datos de contacto", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, placeholder = { Text("Nombre") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, placeholder = { Text("Apellido") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, placeholder = { Text("Email") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, placeholder = { Text("Teléfono") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Dirección de envío", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, placeholder = { Text("Calle y número") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, placeholder = { Text("Ciudad") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = cp, onValueChange = { cp = it }, placeholder = { Text("Código postal") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = pais, onValueChange = { pais = it }, placeholder = { Text("País") }, colors = inputColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Método de envío", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = metodoEnvio == "Estándar",
                        onClick = { metodoEnvio = "Estándar" },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan, unselectedColor = Color.Gray)
                    )
                    Text("Estándar", color = Color.White, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(32.dp))
                    RadioButton(
                        selected = metodoEnvio == "Express",
                        onClick = { metodoEnvio = "Express" },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan, unselectedColor = Color.Gray)
                    )
                    Text("Express", color = Color.White, fontSize = 15.sp)
                }
            }
        }

        Button(
            onClick = {
                val direccionCompleta = "$direccion, $ciudad, $cp, $pais"
                onSiguiente(direccionCompleta)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB13CFF))
        ) {
            Text("Continuar a pago", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FormularioPago(onSiguiente: (String) -> Unit, onAtras: () -> Unit) {
    var numeroTarjeta by remember { mutableStateOf("") }
    var titular by remember { mutableStateOf("") }
    var fechaCaducidad by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.Cyan,
        unfocusedBorderColor = Color(0x33FFFFFF),
        focusedLabelColor = Color.Cyan,
        unfocusedLabelColor = Color.Gray
    )

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Detalles de la tarjeta", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numeroTarjeta,
                    onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                    placeholder = { Text("Número de la tarjeta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = titular,
                    onValueChange = { titular = it },
                    placeholder = { Text("Nombre del titular") },
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = fechaCaducidad,
                        onValueChange = { fechaCaducidad = it },
                        placeholder = { Text("MM/AA") },
                        colors = inputColors,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3) cvv = it },
                        placeholder = { Text("CVV") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = inputColors,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onAtras,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color(0x33FFFFFF))
            ) {
                Text("Atrás", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onSiguiente(numeroTarjeta) },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB13CFF))
            ) {
                Text("Revisar Pedido", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ResumenConfirmacion(
    navController: NavController,
    viewModel: VinylViewModel,
    metodoEnvio: String,
    direccion: String,
    tarjeta: String,
    onConfirmar: () -> Unit
) {
    val items by viewModel.carritoItems.collectAsState()
    val totalProductos = items.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }

    // Usamos el gradiente fuego idéntico al de tu botón de inicio/registro
    val botonGradienteFuego = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF006E), Color(0xFFFF6B00))
    )

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Productos en tu pedido", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                items.forEach { item ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${item.cantidad}x ${item.nombre}", color = Color.LightGray, modifier = Modifier.weight(1f), fontSize = 15.sp)
                        Text(text = "${String.format("%.2f", (item.precio ?: 0.0) * (item.cantidad ?: 1))}€", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información de Entrega", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Método: $metodoEnvio", color = Color.LightGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Dirección: $direccion", color = Color.LightGray, fontSize = 14.sp)
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132330)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Método de pago", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val tarjetaEncriptada = "**** **** **** ${tarjeta.takeLast(4)}"
                Text("Tarjeta vinculada: $tarjetaEncriptada", color = Color.LightGray, fontSize = 14.sp)
            }
        }

        // Bloque del Total
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total a pagar:", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Medium)
            Text("${String.format("%.2f", totalProductos)}€", style = MaterialTheme.typography.headlineMedium, color = Color.Cyan, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón final con tu degradado corporativo de fuego
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(botonGradienteFuego, RoundedCornerShape(16.dp))
                .clickable { onConfirmar() },
            contentAlignment = Alignment.Center
        ) {
            Text("Confirmar y Pagar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}