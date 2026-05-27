package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinilotfg.api.RetrofitClient
import com.example.vinilotfg.model.RegistroRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Registro(navController: NavController) {
    // Estados
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }

    // Diseños
    val fondo = Brush.linearGradient(
        colors = listOf(Color(0xFF4B1173), Color(0xFF1A002D)),
        start = Offset.Zero,
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val botonGradiente = Brush.horizontalGradient(
        colors = listOf(Color(0xFFB13CFF), Color(0xFFFF2D6F))
    )

    Box(modifier = Modifier.fillMaxSize().background(fondo), contentAlignment = Alignment.TopCenter) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(48.dp))
            // Asegúrate de tener LogoTextStyle definido o cámbialo por un estilo estándar
            Text("🎵 Vinyl Sounds", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Crea tu cuenta", fontSize = 14.sp, color = Color(0xFFC9B4E3))
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF221137), RoundedCornerShape(30.dp)).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Registro", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(18.dp))

                // Fila Nombre/Apellido
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, placeholder = { Text("Nombre") }, modifier = Modifier.weight(1f), colors = registroTextFieldColors())
                    OutlinedTextField(value = apellido, onValueChange = { apellido = it }, placeholder = { Text("Apellido") }, modifier = Modifier.weight(1f), colors = registroTextFieldColors())
                }
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, placeholder = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth(), colors = registroTextFieldColors())
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, placeholder = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = registroTextFieldColors())
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = repeatPassword, onValueChange = { repeatPassword = it }, placeholder = { Text("Repite la contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = registroTextFieldColors())

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Botón con gradiente
                Box(modifier = Modifier.fillMaxWidth().height(56.dp).background(botonGradiente, RoundedCornerShape(20.dp))) {
                    Button(
                        onClick = {
                            if (password != repeatPassword) {
                                errorMessage = "Las contraseñas no coinciden."
                            } else {
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        // Preparamos la petición que el servidor de tu compañero espera
                                        val request = RegistroRequest(
                                            email = email,
                                            password = password,
                                            nombre = nombre,
                                            apellidos = apellido
                                        )

                                        // LLAMADA AL NUEVO SERVICIO AUTH
                                        val response = RetrofitClient.authApi.registrarUsuario(request)

                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {
                                                // Si todo fue bien, navegamos a la tienda
                                                navController.navigate("store/$email") {
                                                    popUpTo("inicio") { inclusive = true }
                                                }
                                            } else {
                                                // Capturamos el error específico
                                                val errorBody = response.errorBody()?.string()
                                                errorMessage = "Error ${response.code()}: ${errorBody ?: "No se pudo registrar"}"
                                            }
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            errorMessage = "Error de conexión: ${e.message}"
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun registroTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFF7B5CFF),
    unfocusedBorderColor = Color(0xFF3E2A5E),
    focusedPlaceholderColor = Color(0xFFBFA7D8),
    unfocusedPlaceholderColor = Color(0xFFBFA7D8)
)