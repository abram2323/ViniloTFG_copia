package com.example.vinilotfg.api

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081/"
    private var cookieJar: PersistentCookieJar? = null

    // Debes llamar a este init desde tu MainActivity al iniciar
    fun init(context: Context) {
        cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(cookieJar ?: throw IllegalStateException("RetrofitClient no inicializado. Llama a init(context)"))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Aquí es donde "enchufamos" las cookies
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
}