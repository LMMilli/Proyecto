package com.example.aplicacion.api

import com.example.aplicacion.model.LoginRequest
import com.example.aplicacion.model.RegistroRequest
import com.example.aplicacion.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //Petcion POST a la ruta de UsuarioController

    @POST("api/usuarios/login")
    fun longin(@Body request: LoginRequest): Call<Usuario>

    @POST("api/usuarios")
    fun registrarUsuario(@Body request: RegistroRequest): Call<Usuario>
}