package com.example.puriyayacucho.data

data class Festividad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String, // Formato "dd/MM/yyyy"
    val dia: Int,
    val mes: Int, // 1-12
    val ubicacion: String,
    val videoUrl: String = "",
    val imagenUrl: String = "",
    val imagenesGaleria: List<String> = emptyList(),
    val recomendacion: String = ""
)
