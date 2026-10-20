package com.example.puriyayacucho.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReservaDao {
    @Query("SELECT * FROM reservas WHERE usuarioCorreo = :correo ORDER BY id DESC")
    suspend fun obtenerReservasUsuario(correo: String): List<ReservaEntity>

    @Insert
    suspend fun insertar(reserva: ReservaEntity)

    @Query("DELETE FROM reservas WHERE id = :id")
    suspend fun eliminarReserva(id: Int)
}
