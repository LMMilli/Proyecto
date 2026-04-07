package com.example.aplicacion.model

data class EntrenamientoRequest(
    val usuarioId: Long,
    val nombre: String,
    val detalles: List<DetalleEntrenamientoRequest>
)
