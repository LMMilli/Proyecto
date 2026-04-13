package com.example.aplicacion.model

data class EjercicioEntrenamiento(
    val id: Long,
    val orden: Int,
    val notas: String?,
    val ejercicio: Ejercicio?,
    val series: List<Serie>? = emptyList()
)
