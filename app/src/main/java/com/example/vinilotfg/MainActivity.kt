package com.example.vinilotfg


import DireccionesScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vinilotfg.api.RetrofitClient
import com.example.vinilotfg.ui.screens.*
import com.example.vinilotfg.ui.theme.*
import com.example.vinilotfg.viewmodel.VinylViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(applicationContext)
        enableEdgeToEdge()

        setContent {
            ViniloTFGTheme {
                val navController = rememberNavController()
                val vinylViewModel: VinylViewModel = viewModel()

                NavHost(navController = navController, startDestination = "inicio") {
                    composable("inicio") {
                        InicioScreen(navController, vinylViewModel)
                    }

                    composable("register") {
                        Registro(navController)
                    }

                    composable("mis_pedidos") {
                        MisPedidosScreen(vinylViewModel, navController)
                    }

                    composable("store/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username")
                        StoreScreen(username, navController, vinylViewModel)
                    }

                    composable("store_guest") {
                        StoreScreen(null, navController, vinylViewModel)
                    }

                    composable("detalle/{vinylId}") { backStackEntry ->
                        val vinylId = backStackEntry.arguments?.getString("vinylId")
                        val vinylList by vinylViewModel.vinyls.collectAsState()
                        val producto = vinylList.find { it.id == vinylId }

                        if (producto != null) {
                            DetailScreen(producto, navController, vinylViewModel)
                        }
                    }

                    composable("direcciones") {
                        DireccionesScreen(viewModel = vinylViewModel, navController = navController)
                    }

                    composable("perfil") {
                        ClientesScreen(navController, vinylViewModel)
                    }

                    composable("carrito") {
                        CartScreen(vinylViewModel, navController)
                    }

                    composable("payment_screen") {
                        PaymentScreen(navController, vinylViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun InicioScreen(navController: NavController, vinylViewModel: VinylViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val fondo = Brush.linearGradient(
        colors = listOf(Color(0xFF4B1173), Color(0xFF1A002D)),
        start = Offset.Zero, end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val botonGradiente = Brush.horizontalGradient(
        colors = listOf(Color(0xFFB13CFF), Color(0xFFFF2D6F))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text("🎵 Vinyl Sounds", fontSize = 30.sp, fontWeight = FontWeight.Bold, style = LogoTextStyle)
            Text("Tu música, tu estilo", fontSize = 14.sp, color = Color(0xFFC9B4E3))
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF221137), RoundedCornerShape(30.dp))
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Bienvenido de nuevo", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(18.dp))

                OutlinedButton(
                    onClick = { Toast.makeText(context, "Próximamente disponible", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Text("Continuar con Google", color = Color.Black, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email o usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF7B5CFF),
                        unfocusedBorderColor = Color(0xFF3E2A5E)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF7B5CFF),
                        unfocusedBorderColor = Color(0xFF3E2A5E)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(botonGradiente, RoundedCornerShape(20.dp))
                        .clickable {
                            scope.launch {
                                vinylViewModel.realizarLogin(email, password) { exito, error ->
                                    if (exito) {
                                        navController.navigate("store/false") {
                                            popUpTo("inicio") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, error ?: "Error", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "¿No tienes cuenta? Regístrate",
                    fontSize = 13.sp,
                    color = Color(0xFFBFA7D8),
                    modifier = Modifier.clickable { navController.navigate("register") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Entrar como invitado",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.clickable { navController.navigate("store_guest") }
                )
            }
        }
    }
}