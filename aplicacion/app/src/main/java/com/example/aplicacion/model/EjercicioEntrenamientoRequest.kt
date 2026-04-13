package com.example.aplicacion.model

data class EjercicioEntrenamientoRequest(
    val ejercicioId: Long,
    val equipamientoId: Long? = null,
    val orden: Int,
    val notas: String? = null,
    val series: List<SerieRequest>
)
