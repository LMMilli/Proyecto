package com.example.aplicacion.model

data class EntrenamientoRequest(
    val usuarioId: Long,
    val rutinaId: Long?, //Puede ser nula el entrenamiento no se basa en ninguna rutina
    val duracionMinutos: Int,
    val ejercicios: List<EjercicioEntrenamientoRequest>
)
