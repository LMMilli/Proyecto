package com.example.aplicacion.model

data class Medida(
    val id: Long,
    val pesoCorporal: Double,
    val porcentajeGrasa : Double,
    val fecha: String? = null
)
