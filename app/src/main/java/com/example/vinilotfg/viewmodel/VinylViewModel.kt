package com.example.vinilotfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilotfg.api.CarritoApiService
import com.example.vinilotfg.model.Producto
import com.example.vinilotfg.api.RetrofitClient
import com.example.vinilotfg.model.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.vinilotfg.model.Carrito
import com.example.vinilotfg.model.CarritoRequest
import com.example.vinilotfg.model.Usuario
import retrofit2.Response
class VinylViewModel : ViewModel() {

    private val _vinyls = MutableStateFlow<List<Producto>>(emptyList())
    val vinyls = _vinyls.asStateFlow()

    // --- CAMBIO AQUÍ: Ahora es List<CarritoItem> ---
    private val _carritoItems = MutableStateFlow<List<Carrito>>(emptyList())
    val carritoItems = _carritoItems.asStateFlow()

    // Estado para almacenar el usuario actual
    private val _usuarioPerfil = MutableStateFlow<Usuario?>(null)
    val usuarioPerfil = _usuarioPerfil.asStateFlow()

    var currentUserId: String? = null

    init {
        fetchVinyls()
    }

    private fun fetchVinyls() {
        if (_vinyls.value.isNotEmpty()) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.productoApi.getProductos()
                _vinyls.value = response
            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Error al cargar vinilos: ${e.message}")
            }
        }
    }

    //Funciones para Usuario
    fun realizarLogin(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.loginUsuario(LoginRequest(email, pass))

                // LOG DE ORO: Aquí veremos qué trae la respuesta
                android.util.Log.d("DEBUG_LOGIN", "Código: ${response.code()}")
                android.util.Log.d("DEBUG_LOGIN", "Body: ${response.body()}")
                android.util.Log.e("DEBUG_LOGIN", "ErrorBody: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    // Aquí el servidor dice que todo salió bien (200)
                    // Asegúrate de que no estás intentando castear a algo que no es
                    onResult(true, null)
                } else {
                    onResult(false, "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }

    // Función para llamar a la API
    fun obtenerPerfil() {
        viewModelScope.launch {
            try {
                // El endpoint /api/auth/me usa la cookie automáticamente
                val response = RetrofitClient.usuarioApi.obtenerMiPerfil()

                if (response.isSuccessful && response.body() != null) {
                    // Si la respuesta es un Map, GSON suele convertirlo mal a Usuario
                    // así que vamos a asegurar los datos manualmente:
                    val data = response.body() as Map<String, Any>
                    _usuarioPerfil.value = Usuario(
                        nombre = data["nombre"]?.toString() ?: "Sin nombre",
                        email = data["email"]?.toString() ?: "Sin email"
                    )
                    android.util.Log.d("DEBUG_PERFIL", "Perfil cargado: ${data["nombre"]}")
                } else {
                    android.util.Log.e(
                        "DEBUG_PERFIL",
                        "Error: ${response.code()} - Verifica si iniciaste sesión correctamente"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_PERFIL", "Error: ${e.message}")
            }
        }
    }

    fun cerrarSesion(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // 1. Avisar al servidor
                val response = RetrofitClient.authApi.logout()

                // 2. Limpiar cookies usando el nuevo método (sin pasar null)
                RetrofitClient.limpiarCookies()

                // 3. Resetear el estado (Ahora funcionará porque pusimos el ? en el StateFlow)
                _usuarioPerfil.value = null

                onResult(response.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
    //----------------------------------------------------------------------------------------------

    //Funciones del Carrrito
    fun agregarAlCarrito(productoId: String) {
        viewModelScope.launch {
            try {
                // Usamos la clase de datos, no un mapa
                val request = CarritoRequest(productoId, 1)

                val response = RetrofitClient.carritoApi.agregarProducto(request)

                if (response.isSuccessful) {
                    android.util.Log.d("DEBUG_CARRITO", "Éxito: Producto añadido")
                    obtenerCarrito()
                } else {
                    android.util.Log.e("DEBUG_CARRITO", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_CARRITO", "Excepción: ${e.message}")
            }
        }
    }

    fun obtenerCarrito() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.carritoApi.obtenerCarrito()
                if (response.isSuccessful) {
                    // Ahora esto es compatible con _carritoItems (List<CarritoItem>)
                    _carritoItems.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Error al listar carrito: ${e.message}")
            }
        }
    }

    fun actualizarCantidad(itemId: String?, nuevaCantidad: Int) {
        if (itemId == null || nuevaCantidad < 1) return

        viewModelScope.launch {
            try {
                // Ahora esto es explícitamente un Map<String, Int>
                val body = mapOf("cantidad" to nuevaCantidad)

                val response = RetrofitClient.carritoApi.actualizarCantidad(itemId, body)

                if (response.isSuccessful) {
                    obtenerCarrito()
                } else {
                    android.util.Log.e("DEBUG_CARRITO", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_CARRITO", "Error de red: ${e.message}")
            }
        }
    }

    fun eliminarProducto(itemId: String?) {
        if (itemId == null) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.carritoApi.eliminarItem(itemId)

                if (response.isSuccessful) {
                    // Al eliminar, refrescamos el carrito para que desaparezca de la UI
                    obtenerCarrito()
                } else {
                    android.util.Log.e("DEBUG_CARRITO", "Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_CARRITO", "Error de red: ${e.message}")
            }
        }
    }
    //-------------------------------------------------------------------------------------------------
}