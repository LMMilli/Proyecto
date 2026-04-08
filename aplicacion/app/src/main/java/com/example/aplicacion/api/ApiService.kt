package com.example.aplicacion.api

import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Entrenamiento
import com.example.aplicacion.model.EntrenamientoRequest
import com.example.aplicacion.model.LoginRequest
import com.example.aplicacion.model.Medida
import com.example.aplicacion.model.MedidaRequest
import com.example.aplicacion.model.RegistroRequest
import com.example.aplicacion.model.Rutina
import com.example.aplicacion.model.RutinaRequest
import com.example.aplicacion.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //Petcion POST a la ruta de UsuarioController

    @POST("api/usuarios/login")
    fun longin(@Body request: LoginRequest): Call<Usuario>

    @POST("api/usuarios")
    fun registrarUsuario(@Body request: RegistroRequest): Call<Usuario>

    @POST("api/ejercicios")
    fun crearEjercicio(@Body ejercicio: Ejercicio): Call<Ejercicio>

    @GET("api/ejercicios")
    fun obtenerEjercicios(): Call<List<Ejercicio>>

    @POST("api/rutinas")
    fun crearRutina(@Body request: RutinaRequest): Call<Rutina>

    @GET("api/rutinas")
    fun obtenerTodasLasRutinas(): Call<List<Rutina>>

    @POST("api/medidas")
    fun registarMedida(@Body request: MedidaRequest):Call<Void>

    @GET("api/medidas/usuario/{id}")
    fun obtenerMedidas(@Path("id") idUsuario: Long): Call<List<Medida>>

    @POST("api/entrenamientos")
    fun guardarEntrenamiento(@Body request: EntrenamientoRequest): Call<Void>

    @GET("api/entrenamientos/usuario/{id}")
    fun obtenerHistorialEntrenamientos(@Path("id") idUsuario: Long): Call<List<Entrenamiento>>

    @GET("api/entrenamientos/{id}")
    fun obtenerDetallesEntrenamiento(@Path("id") idEntrenamiento: Long): Call<Entrenamiento>
}