package com.example.aplicacion

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de OkHttp encargado de inyectar el token de autenticación
 * en todas las peticiones salientes que requieren autorización.
 *
 * @param tokenManager Instancia encargada de gestionar el almacenamiento y recuperación del token.
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtenemos la solicitud original y creamos un constructor para modificarla
        val requestBuilder = chain.request().newBuilder()

        // Recuperamos el token almacenado (ej. desde SharedPreferences o DataStore)
        val token = tokenManager.getToken()

        // Si el token es válido, lo inyectamos en la cabecera 'Authorization'
        // utilizando el esquema Bearer estándar para JWT.
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Procedemos con la petición modificada
        return chain.proceed(requestBuilder.build())
    }
}