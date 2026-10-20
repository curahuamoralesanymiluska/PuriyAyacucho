package com.example.puriyayacucho.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

/** Un recorrido guardado de la Guía personalizada. */
@Entity(tableName = "recorridos")
data class RecorridoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioCorreo: String,
    val titulo: String,
    val modo: String,
    /** Puntos del recorrido en JSON: [{"n":"Plaza","lat":-13.16,"lng":-74.22}, ...] */
    val waypointsJson: String,
    val fecha: String
)

@Dao
interface RecorridoDao {
    @Query("SELECT * FROM recorridos WHERE usuarioCorreo = :correo ORDER BY id DESC")
    suspend fun obtenerRecorridos(correo: String): List<RecorridoEntity>

    @Insert
    suspend fun insertar(recorrido: RecorridoEntity)

    @Query("DELETE FROM recorridos WHERE id = :id")
    suspend fun eliminar(id: Int)
}
