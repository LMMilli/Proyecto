package com.example.aplicacion.model

data class DetalleEntrenamientoRequest(
    val ejercicioId : Long,
    val series: Int,
    val repeticiones: Int,
    val peso: Double
)
