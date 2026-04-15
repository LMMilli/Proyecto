package com.example.aplicacion.model

data class Equipamiento(
    val id: Long,
    val nombre: String
){
    override fun toString(): String = nombre
}
