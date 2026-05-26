package com.example.aplicacion.api

import com.example.aplicacion.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Interfaz de definición de puntos de acceso (endpoints) de la API REST.
 * Esta interfaz es implementada por Retrofit para realizar las peticiones HTTP
 * hacia el servidor backend de GymProgress.
 */
interface ApiService {

    // --- Autenticación y Usuarios ---
    @POST("api/usuarios/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("api/usuarios")
    fun registrarUsuario(@Body request: RegistroRequest): Call<Usuario>

    // --- Ejercicios ---
    @POST("api/ejercicios")
    fun crearEjercicio(@Body ejercicio: Ejercicio): Call<Ejercicio>

    @GET("api/ejercicios")
    fun obtenerEjercicios(): Call<List<Ejercicio>>

    // --- Rutinas ---
    @POST("api/rutinas")
    fun crearRutina(@Body request: RutinaRequest): Call<Rutina>

    @GET("api/rutinas")
    fun obtenerTodasLasRutinas(): Call<List<Rutina>>

    // --- Medidas y Progreso ---
    @POST("api/medidas")
    fun registarMedida(@Body request: MedidaRequest): Call<Void>

    @GET("api/medidas/usuario/{id}")
    fun obtenerMedidas(@Path("id") idUsuario: Long): Call<List<Medida>>

    // --- Entrenamientos ---
    @POST("api/entrenamientos")
    fun guardarEntrenamiento(@Body request: EntrenamientoRequest): Call<Void>

    @GET("api/entrenamientos/usuario/{id}")
    fun obtenerHistorialEntrenamientos(@Path("id") idUsuario: Long): Call<List<Entrenamiento>>

    @GET("api/entrenamientos/{id}")
    fun obtenerDetallesEntrenamiento(@Path("id") idEntrenamiento: Long): Call<Entrenamiento>

    // --- Equipamiento ---
    @GET("api/equipamiento")
    fun obtenerTodosEquipamientos(): Call<List<Equipamiento>>

    // --- Objetivos ---
    @POST("api/objetivos")
    fun crearObjetivo(@Body request: ObjetivoRequest): Call<Objetivo>

    @GET("api/objetivos/usuario/{id}")
    fun obtenerObjetivos(@Path("id") idUsuario: Long): Call<List<Objetivo>>

    @PUT("api/objetivos/{id}/estado")
    fun actualizarEstadoObjetivo(
        @Path("id") id: Long,
        @Query("completado") completado: Boolean
    ): Call<Objetivo>
}