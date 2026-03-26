package com.example.aplicacion.model

import com.google.gson.annotations.SerializedName

data class Rutina(
    val id: Long,
    val nombre : String,
    @SerializedName("ejercicios")
    val ejercicio: List<Ejercicio>
)
