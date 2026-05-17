package com.example.aplicacion.model

data class ObjetivoRequest(
    val usuarioId: Long,
    val tipo: String,
    val valorObjetivo: Double,
    val fechaLimite: String
)
