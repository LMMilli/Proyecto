package com.example.aplicacion.model

data class Entrenamiento(
    val id: Long,
    val duracion: Int,
    val fecha: String,
    val rutinaId: Int? = null,
    val usuarioId: Int
)
