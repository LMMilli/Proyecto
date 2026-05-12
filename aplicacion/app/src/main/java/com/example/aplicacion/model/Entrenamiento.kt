package com.example.aplicacion.model

import com.google.gson.annotations.SerializedName

data class Entrenamiento(
    val id: Long,
    @SerializedName("duracionMinutos") // Mapeamos el nombre del JSON
    val duracion: Int,
    val fecha: String,
    val rutinaId: Int? = null,
    @SerializedName("ejercicios") // Mapeamos el nombre del JSON
    val ejerciciosEntrenamiento: List<EjercicioEntrenamiento>?= emptyList()
)