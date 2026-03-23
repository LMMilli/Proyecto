package com.example.aplicacion.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //10.0.2.2 es el localhost para el emulador de Android
    //Para  usar el movil hay que poner la ipa del ordenador

    private const val BASE_URL = "http://192.168.1.51:8080/"

    //"by lazy" hace que retrofit solo se construya la primera vez para ahorrar memoria
    val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}