package com.example.vinilotfg.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinilotfg.viewmodel.VinylViewModel // Asegúrate de que esta ruta sea la correcta para tu proyecto
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.google.gson.Gson
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PaymentScreen(navController: NavController, viewModel: VinylViewModel) {
    var pasoActual by remember { mutableIntStateOf(1) }

    var direccion by remember { mutableStateOf("") }
    var metodoEnvio by remember { mutableStateOf("Estándar") }
    var tarjetaLast4 by remember { mutableStateOf("4242") }
    val fondoOscuro = Color(0xFF120338)

    val usuarioActual by viewModel.usuarioPerfil.collectAsState()
    val context = LocalContext.current
    val items by viewModel.carritoItems.collectAsState()
    val total = items.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }

    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
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
                                // 1. Llamamos a la función que vacía el carrito en el servidor y local
                                viewModel.limpiarCarritoEnServidor()

                                // 2. Navegamos (esto ya lo tenías)
                                val nombre = usuarioActual?.nombre
                                if (!nombre.isNullOrEmpty() && nombre != "Sin nombre") {
                                    android.widget.Toast.makeText(context, "¡Pedido realizado con éxito!", android.widget.Toast.LENGTH_LONG).show()
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

@Composable
fun FormularioEnvio(onSiguiente: (String) -> Unit) {
    // Estados para cada campo
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var metodoEnvio by remember { mutableStateOf("Estándar") }



    Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // scroll necesario para muchos campos
        Text("Datos de contacto", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        Text("Dirección", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Calle y número") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = cp, onValueChange = { cp = it }, label = { Text("Código postal") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pais, onValueChange = { pais = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        Text("Método de envío", style = MaterialTheme.typography.titleLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = metodoEnvio == "Estándar", onClick = { metodoEnvio = "Estándar" })
            Text("Estándar")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = metodoEnvio == "Express", onClick = { metodoEnvio = "Express" })
            Text("Express")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Aquí podrías validar que los campos no estén vacíos
                val direccionCompleta = "$direccion, $ciudad, $cp, $pais"
                onSiguiente(direccionCompleta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar a pago")
        }
    }
}

@Composable
fun FormularioPago(onSiguiente: (String) -> Unit, onAtras: () -> Unit) {
    var numeroTarjeta by remember { mutableStateOf("") }
    var titular by remember { mutableStateOf("") }
    var fechaCaducidad by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Detalles de la tarjeta", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numeroTarjeta,
            onValueChange = { if (it.length <= 16) numeroTarjeta = it },
            label = { Text("Número de la tarjeta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = titular,
            onValueChange = { titular = it },
            label = { Text("Nombre del titular") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = fechaCaducidad,
                onValueChange = { fechaCaducidad = it },
                label = { Text("MM/AA") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = cvv,
                onValueChange = { if (it.length <= 3) cvv = it },
                label = { Text("CVV") },
                visualTransformation = PasswordVisualTransformation(), // Oculta el CVV
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onAtras) { Text("Atrás") }
            Button(onClick = { onSiguiente(numeroTarjeta) }) {
                Text("Revisar Pedido")
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
    tarjeta: String, // Recibimos el número completo para encriptarlo aquí
    onConfirmar: () -> Unit
) {
    val context = LocalContext.current
    val items by viewModel.carritoItems.collectAsState()
    val totalProductos = items.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 1) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Resumen de tu pedido", style = MaterialTheme.typography.headlineSmall)

        // 1. Productos
        items.forEach { item ->
            Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                // Si tienes una URL de imagen, usarías un AsyncImage (coil)
                Text(text = "${item.cantidad}x ${item.nombre}", modifier = Modifier.weight(1f))
                Text(text = "${String.format("%.2f", (item.precio ?: 0.0) * (item.cantidad ?: 1))}€")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 2. Envío y Dirección
        Text("Envío", style = MaterialTheme.typography.titleMedium)
        Text("Método: $metodoEnvio")
        Text("Dirección: $direccion")

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 3. Tarjeta encriptada
        Text("Método de pago", style = MaterialTheme.typography.titleMedium)
        val tarjetaEncriptada = "**** **** **** ${tarjeta.takeLast(4)}"
        Text("Tarjeta: $tarjetaEncriptada")

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 4. Total
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total a pagar:", style = MaterialTheme.typography.titleLarge)
            Text("${String.format("%.2f", totalProductos)}€", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onConfirmar()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar y Pagar")
        }
    }
}