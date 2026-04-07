package com.example.aplicacion.model

data class MedidaRequest(
    val usuarioId: Long,
    val peso: Double,
    val porcentajeGrasa: Double
)
