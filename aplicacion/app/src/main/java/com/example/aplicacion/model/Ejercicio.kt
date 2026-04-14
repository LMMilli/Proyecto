package com.example.aplicacion.model

import com.google.gson.annotations.SerializedName

data class Ejercicio(
    val id: Long? = null, //Cunado se crea el id es null, cuando nos lo devuelve el servidor devulve un Long
    val nombre: String,
    val grupoMuscular: String,
    val descripcion : String,

    @SerializedName("equiposDisponibles")
    val equipamiento: List<Equipamiento>
)
