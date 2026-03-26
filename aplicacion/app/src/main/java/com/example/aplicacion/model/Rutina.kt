package com.example.aplicacion.model

data class Rutina(
    val id: Long,
    val nombre : String,
    val ejercicio: List<Ejercicio>
)
