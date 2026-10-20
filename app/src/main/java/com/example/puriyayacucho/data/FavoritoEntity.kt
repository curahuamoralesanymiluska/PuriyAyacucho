package com.example.puriyayacucho.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioCorreo: String,
    val lugarId: Int,
    val nombre: String,
    val imagenUrl: String,
    val imagenRes: Int = 0, // Campo para imágenes locales PNG
    val tipo: String
)
