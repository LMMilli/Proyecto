package com.example.aplicacion.model

data class EjercicioEntrenamiento(
    val id: Long,
    val orden: Int,
    val notas: String?,
    val nombreEjercicio: String?,       // Añadido para coincidir con el JSON
    val nombreEquipamiento: String?,    // Añadido para coincidir con el JSON
    val series: List<Serie>? = emptyList()
)