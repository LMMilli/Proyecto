package com.example.aplicacion

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response{
        val requestBuilder = chain.request().newBuilder()

        //Scamos el token
        val token = tokenManager.getToken()

        //Si existe, se lo inyectamos a la cabecera
        if (token != null){
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}