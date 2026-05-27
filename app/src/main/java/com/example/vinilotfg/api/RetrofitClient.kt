package com.example.vinilotfg.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ASEGÚRATE DE QUE ESTO SE LLAME IGUAL QUE EN TU VIEWMODEL
    val productoApi: ProductoApiService = retrofit.create(ProductoApiService::class.java)

    // Tus otros servicios (auth, carrito, etc.)
    val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
    val carritoApi: CarritoApiService = retrofit.create(CarritoApiService::class.java)
}