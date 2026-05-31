package com.example.vinilotfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinilotfg.api.RetrofitClient
import com.example.vinilotfg.model.RegistroRequest
import com.example.vinilotfg.ui.theme.LogoTextStyle
import com.example.vinilotfg.viewmodel.VinylViewModel // Asegúrate de que esta ruta sea la de tu proyecto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Registro(navController: NavController, viewModel: VinylViewModel) { // 👈 Recibe el viewModel aquí
    // Estados
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // Estados para controlar la visibilidad de los ojitos (independientes)
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }

    // Diseños
    val fondoDegradado = Brush.linearGradient(
        colors = listOf(Color(0xFF08050F), Color(0xFF0C0918), Color(0xFF12103A)),
        start = Offset(0f, 0f),
        end = Offset(0f, 2000f)
    )

    val degradadoLogo = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF0EBFF),
            Color(0xFF7B2FFF),
            Color(0xFFFF006E),
            Color(0xFFFF6B00)
        )
    )

    val botonGradiente = Brush.horizontalGradient(
        colors = listOf(Color(0xFFB13CFF), Color(0xFFFF2D6F))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoDegradado),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Vinyl Sounds",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                style = LogoTextStyle.copy(
                    brush = degradadoLogo
                )
            )
            Text("Crea tu cuenta", fontSize = 14.sp, color = Color(0xFFC9B4E3))

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF221137), RoundedCornerShape(30.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Registro", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(18.dp))

                // Fila Nombre/Apellido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        placeholder = { Text("Nombre") },
                        modifier = Modifier.weight(1f),
                        colors = registroTextFieldColors()
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        placeholder = { Text("Apellido") },
                        modifier = Modifier.weight(1f),
                        colors = registroTextFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = registroTextFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar" else "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = registroTextFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    placeholder = { Text("Repite la contraseña") },
                    singleLine = true,
                    visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (repeatPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = if (repeatPasswordVisible) "Ocultar" else "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = registroTextFieldColors()
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Botón con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(botonGradiente, RoundedCornerShape(20.dp))
                ) {
                    Button(
                        onClick = {
                            if (password != repeatPassword) {
                                errorMessage = "Las contraseñas no coinciden."
                            } else {
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        val request = RegistroRequest(
                                            email = email,
                                            password = password,
                                            nombre = nombre,
                                            apellidos = apellido
                                        )

                                        // 1. Mandamos el registro a Supabase
                                        val response = RetrofitClient.authApi.registrarUsuario(request)

                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {

                                                // 2. 🚀 LOGIN AUTOMÁTICO TRAS REGISTRO EXITOSO 🚀
                                                viewModel.realizarLogin(email, password) { loginExitoso, errorMsg ->
                                                    if (loginExitoso) {
                                                        // 3. Ya con la cookie activa, cargamos el Perfil en memoria
                                                        viewModel.obtenerPerfil()

                                                        // 4. Saltamos a la Store con los datos listos
                                                        navController.navigate("store/$email") {
                                                            popUpTo("inicio") { inclusive = true }
                                                        }
                                                    } else {
                                                        // Fallback por seguridad: si falla el login automático manda a login
                                                        errorMessage = "Cuenta creada. Por favor, inicia sesión."
                                                        navController.navigate("login") {
                                                            popUpTo("inicio") { inclusive = true }
                                                        }
                                                    }
                                                }

                                            } else {
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