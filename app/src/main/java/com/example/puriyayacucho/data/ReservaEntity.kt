package com.example.puriyayacucho.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioCorreo: String,
    val lugarId: Int,
    val nombreLugar: String,
    val fecha: String,
    val imagenRes: Int,
    val estado: String = "Pendiente", // Pendiente, Completada, Cancelada
    val numPersonas: Int = 1
)
