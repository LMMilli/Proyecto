package com.example.aplicacion.model

data class DetalleEntrenamientoRequest(
    val ejercicioId : Long,
    val serires: Int,
    val repeticiones: Int,
    val peso: Double
)
