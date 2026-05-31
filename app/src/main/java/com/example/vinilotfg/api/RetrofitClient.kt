package com.example.vinilotfg.api

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://vinylsounds.onrender.com/"
    private var cookieJar: PersistentCookieJar? = null

    // Debes llamar a este init desde tu MainActivity al iniciar
    fun init(context: Context) {
        cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    private fun getClient(): OkHttpClient {
        val jar = cookieJar ?: throw IllegalStateException("RetrofitClient no inicializado")
        return OkHttpClient.Builder()
            .cookieJar(jar)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient()) // Ahora llamamos a la función que recupera el jar
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val usuarioApi: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    // Dentro de tu object RetrofitClient
    fun limpiarCookies() {
        cookieJar?.clear() // Esto borra las cookies sin necesitar un Context
    }

    val productoApi: ProductoApiService by lazy { retrofit.create(ProductoApiService::class.java) }
    val authApi: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    val carritoApi: CarritoApiService by lazy { retrofit.create(CarritoApiService::class.java) }
    val pedidoApi: PedidoApiService by lazy { retrofit.create(PedidoApiService::class.java) }
    val direccionApi: DireccionApiService by lazy {
        retrofit.create(DireccionApiService::class.java)
    }
}