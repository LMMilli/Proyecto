package com.example.aplicacion.api

import android.content.Context
import com.example.aplicacion.AuthInterceptor
import com.example.aplicacion.MyApp
import com.example.aplicacion.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //10.0.2.2 es el localhost para el emulador de Android
    //Para  usar el movil hay que poner la ipa del ordenador

    private const val BASE_URL = "http://10.0.2.2:8080/"

    val retrofit: Retrofit by lazy{

        val tokenManager = TokenManager(MyApp.appContext)
        val authInterceptor = AuthInterceptor(tokenManager)

        //Añadimos el interceptor al cliente HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}