package com.example.aplicacion.model

data class Objetivo(
    val id: Long? = null,
    val tipo: String,
    val valorObjetivo: Double,
    val fechaLimite: String,
    val completado: Boolean
)
