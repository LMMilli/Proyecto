package com.example.aplicacion.model

data class SerieRequest(
    val repeticiones: Int,
    val peso: Double,
    val rpe: Int,
    val tipo: String? = "Efectiva"
)
