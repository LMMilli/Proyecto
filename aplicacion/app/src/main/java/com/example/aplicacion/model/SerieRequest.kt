package com.example.aplicacion.model

data class SerieRequest(
    val ejercicioId: Long,
    val repeticiones: Int,
    val peso: Double,
    val rpe: Int
)
