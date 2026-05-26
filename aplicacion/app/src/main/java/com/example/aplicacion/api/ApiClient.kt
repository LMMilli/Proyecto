package com.example.aplicacion.api

import com.example.aplicacion.AuthInterceptor
import com.example.aplicacion.MyApp
import com.example.aplicacion.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de red centralizado para la aplicación.
 * Utiliza el patrón Singleton para configurar Retrofit con soporte de interceptores
 * de autenticación y conversión automática de JSON mediante Gson.
 */
object ApiClient {

    // Dirección base del servidor.
    // Nota: '10.0.2.2' permite al emulador de Android acceder al localhost (8080) del PC.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    /**
     * Instancia de Retrofit inicializada de forma perezosa (lazy).
     * Configura el cliente OkHttp con un [AuthInterceptor] para inyectar tokens automáticamente.
     */
    val retrofit: Retrofit by lazy {

        val tokenManager = TokenManager(MyApp.appContext)
        val authInterceptor = AuthInterceptor(tokenManager)

        // Configuración del cliente HTTP con el interceptor de seguridad
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        // Construcción de la instancia de Retrofit
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Conversor de JSON a objetos Kotlin
            .build()
    }
}