package com.example.aplicacion.model

data class Ejercicio(
    val id: Long? = null, //Cunado se crea el id es null, cuando nos lo devuelve el servidor devulve un Long
    val nombre: String,
    val grupoMuscular: String,
    val descripcion : String
)
