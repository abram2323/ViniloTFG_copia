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
class VinylViewModel : ViewModel() {

    private val _vinyls = MutableStateFlow<List<Producto>>(emptyList())
    val vinyls = _vinyls.asStateFlow()

    // --- CAMBIO AQUÍ: Ahora es List<CarritoItem> ---
    private val _carritoItems = MutableStateFlow<List<Carrito>>(emptyList())
    val carritoItems = _carritoItems.asStateFlow()

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

    fun realizarLogin(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, pass)
                val response = RetrofitClient.authApi.loginUsuario(request)

                if (response.isSuccessful && response.body() != null) {
                    onResult(true, null)
                } else {
                    onResult(false, "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                onResult(false, "Error de conexión: ${e.message}")
            }
        }
    }

    fun agregarAlCarrito(productoId: String) {
        viewModelScope.launch {
            try {
                val body = mapOf("productoId" to productoId, "cantidad" to 1)
                android.util.Log.d("DEBUG_CARRITO", "Intentando añadir: $productoId")

                val response = RetrofitClient.carritoApi.agregarProducto(body)

                if (response.isSuccessful) {
                    android.util.Log.d("DEBUG_CARRITO", "Éxito: Producto añadido")
                    obtenerCarrito()
                } else {
                    // Aquí es donde veremos si el servidor nos dice por qué falla (ej: 401, 403, 500)
                    android.util.Log.e("DEBUG_CARRITO", "Error del servidor: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_CARRITO", "Excepción de red: ${e.message}")
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
}